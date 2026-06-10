package br.edu.ifsudeste.demo.api.controller;

import br.edu.ifsudeste.demo.api.dto.FuncionarioDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Funcionario;
import br.edu.ifsudeste.demo.model.service.FuncionarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/funcionarios")
@RequiredArgsConstructor
@Tag(
        name = "Funcionários",
        description = "Operações relacionadas ao gerenciamento de funcionários"
)
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @Operation(
            summary = "Listar funcionários",
            description = "Retorna todos os funcionários cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Funcionários listados com sucesso")
    @GetMapping()
    public ResponseEntity get() {

        List<Funcionario> funcionarios = funcionarioService.getFuncionario();

        return ResponseEntity.ok(
                funcionarios.stream()
                        .map(FuncionarioDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "Buscar funcionário por ID",
            description = "Retorna os dados de um funcionário específico."
    )
    @ApiResponse(responseCode = "200", description = "Funcionário encontrado")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {

        Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(id);

        if (!funcionario.isPresent()) {
            return new ResponseEntity("Funcionário não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(funcionario.map(FuncionarioDTO::create));
    }

    @Operation(
            summary = "Cadastrar funcionário",
            description = "Realiza o cadastro de um novo funcionário."
    )
    @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PostMapping()
    public ResponseEntity post(@RequestBody FuncionarioDTO dto) {

        try {
            Funcionario funcionario = converter(dto);
            funcionario = funcionarioService.salvar(funcionario);

            return new ResponseEntity(funcionario, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Funcionario converter(FuncionarioDTO dto) {

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Funcionario.class);
    }

    @Operation(
            summary = "Atualizar funcionário",
            description = "Atualiza os dados de um funcionário já cadastrado."
    )
    @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody FuncionarioDTO dto) {

        if (!funcionarioService.getFuncionarioById(id).isPresent()) {
            return new ResponseEntity("Funcionário não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            Funcionario funcionario = converter(dto);
            funcionario.setId(id);

            funcionarioService.salvar(funcionario);

            return ResponseEntity.ok(funcionario);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Excluir funcionário",
            description = "Remove um funcionário do sistema."
    )
    @ApiResponse(responseCode = "204", description = "Funcionário removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(id);

        if (!funcionario.isPresent()) {
            return new ResponseEntity("Funcionário não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            funcionarioService.excluir(funcionario.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}