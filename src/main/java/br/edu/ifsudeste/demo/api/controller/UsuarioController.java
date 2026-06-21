package br.edu.ifsudeste.demo.api.controller;

import br.edu.ifsudeste.demo.api.dto.CredenciaisDTO;
import br.edu.ifsudeste.demo.api.dto.TokenDTO;
import br.edu.ifsudeste.demo.api.dto.UsuarioDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.exception.SenhaInvalidaException;
import br.edu.ifsudeste.demo.model.entity.Usuario;
import br.edu.ifsudeste.demo.model.service.UsuarioService;
import br.edu.ifsudeste.demo.security.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin
@Tag(
        name = "Usuários",
        description = "Operações relacionadas ao gerenciamento e autenticação de usuários"
)
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioService service;

    @Operation(
            summary = "Listar usuários",
            description = "Retorna todos os usuários cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso")
    @GetMapping()
    public ResponseEntity get() {

        List<Usuario> usuarios = service.getUsuarios();

        return ResponseEntity.ok(
                usuarios.stream()
                        .map(UsuarioDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico."
    )
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {

        Optional<Usuario> usuario = service.getUsuarioById(id);

        if (!usuario.isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(usuario.map(UsuarioDTO::create));
    }

    @Operation(
            summary = "Cadastrar usuário",
            description = "Realiza o cadastro de um novo usuário."
    )
    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PostMapping()
    public ResponseEntity post(@RequestBody UsuarioDTO dto) {

        try {

            if (dto.getSenha() == null || dto.getSenha().trim().equals("") ||
                    dto.getSenhaRepeticao() == null || dto.getSenhaRepeticao().trim().equals("")) {
                return ResponseEntity.badRequest().body("Senha inválida");
            }

            if (!dto.getSenha().equals(dto.getSenhaRepeticao())) {
                return ResponseEntity.badRequest().body("Senhas não conferem");
            }

            Usuario usuario = converter(dto);

            String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
            usuario.setSenha(senhaCriptografada);

            usuario = service.salvar(usuario);

            return new ResponseEntity(usuario, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza o login e retorna um token JWT."
    )
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Login ou senha inválidos")
    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
        System.out.println("ENTROU NO AUTH");
        try {

            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha())
                    .build();

            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);

            String token = jwtService.gerarToken(usuario);

            return new TokenDTO(usuario.getLogin(), token);

        } catch (UsernameNotFoundException | SenhaInvalidaException e) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage()
            );
        }
    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados de um usuário já cadastrado."
    )
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PutMapping("{id}")
    public ResponseEntity atualizar(
            @PathVariable("id") Long id,
            @RequestBody UsuarioDTO dto) {

        if (!service.getUsuarioById(id).isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }

        try {

            if (dto.getSenha() == null || dto.getSenha().trim().equals("") ||
                    dto.getSenhaRepeticao() == null || dto.getSenhaRepeticao().trim().equals("")) {
                return ResponseEntity.badRequest().body("Senha inválida");
            }

            if (!dto.getSenha().equals(dto.getSenhaRepeticao())) {
                return ResponseEntity.badRequest().body("Senhas não conferem");
            }

            Usuario usuario = converter(dto);
            usuario.setId(id);

            service.salvar(usuario);

            return ResponseEntity.ok(usuario);

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Excluir usuário",
            description = "Remove um usuário do sistema."
    )
    @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        Optional<Usuario> usuario = service.getUsuarioById(id);

        if (!usuario.isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }

        try {

            service.excluir(usuario.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Usuario converter(UsuarioDTO dto) {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(dto, Usuario.class);
    }
}