package br.edu.ifsudeste.demo.api.controller;

import br.edu.ifsudeste.demo.api.dto.ConsertoDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.*;
import br.edu.ifsudeste.demo.model.service.ClienteService;
import br.edu.ifsudeste.demo.model.service.ConsertoService;
import br.edu.ifsudeste.demo.model.service.DispositivoService;
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
@RequestMapping("/api/v1/consertos")
@RequiredArgsConstructor
@Tag(
        name = "Consertos",
        description = "Operações relacionadas ao gerenciamento de consertos"
)
public class ConsertoController {

    private final ConsertoService consertoService;
    private final ClienteService clienteService;
    private final DispositivoService dispositivoService;
    private final FuncionarioService funcionarioService;

    @Operation(
            summary = "Listar consertos",
            description = "Retorna todos os consertos cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Consertos listados com sucesso")
    @GetMapping()
    public ResponseEntity get() {

        List<Conserto> consertos = consertoService.getConserto();

        return ResponseEntity.ok(
                consertos.stream()
                        .map(ConsertoDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "Buscar conserto por ID",
            description = "Retorna os dados de um conserto específico."
    )
    @ApiResponse(responseCode = "200", description = "Conserto encontrado")
    @ApiResponse(responseCode = "404", description = "Conserto não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {

        Optional<Conserto> conserto = consertoService.getConsertoById(id);

        if (!conserto.isPresent()) {
            return new ResponseEntity("Conserto não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(conserto.map(ConsertoDTO::create));
    }

    @Operation(
            summary = "Cadastrar conserto",
            description = "Realiza o cadastro de um novo conserto."
    )
    @ApiResponse(responseCode = "201", description = "Conserto cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PostMapping()
    public ResponseEntity post(@RequestBody ConsertoDTO dto) {

        try {
            Conserto conserto = converter(dto);
            conserto = consertoService.salvar(conserto);

            return new ResponseEntity(conserto, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Conserto converter(ConsertoDTO dto) {

        ModelMapper modelMapper = new ModelMapper();
        Conserto conserto = modelMapper.map(dto, Conserto.class);

        if (dto.getIdCliente() != null) {

            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());

            if (!cliente.isPresent()) {
                conserto.setCliente(null);
            } else {
                conserto.setCliente(cliente.get());
            }
        }

        if (dto.getIdDispositivo() != null) {

            Optional<Dispositivo> dispositivo =
                    dispositivoService.getDispositivoById(dto.getIdDispositivo());

            if (!dispositivo.isPresent()) {
                conserto.setDispositivo(null);
            } else {
                conserto.setDispositivo(dispositivo.get());
            }
        }

        if (dto.getIdFuncionario() != null) {

            Optional<Funcionario> funcionario =
                    funcionarioService.getFuncionarioById(dto.getIdFuncionario());

            if (!funcionario.isPresent()) {
                conserto.setFuncionario(null);
            } else {
                conserto.setFuncionario(funcionario.get());
            }
        }

        return conserto;
    }

    @Operation(
            summary = "Atualizar conserto",
            description = "Atualiza os dados de um conserto já cadastrado."
    )
    @ApiResponse(responseCode = "200", description = "Conserto atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Conserto não encontrado")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody ConsertoDTO dto) {

        if (!consertoService.getConsertoById(id).isPresent()) {
            return new ResponseEntity("Conserto não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            Conserto conserto = converter(dto);
            conserto.setId(id);

            consertoService.salvar(conserto);

            return ResponseEntity.ok(conserto);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Excluir conserto",
            description = "Remove um conserto do sistema."
    )
    @ApiResponse(responseCode = "204", description = "Conserto removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Conserto não encontrado")
    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Conserto> conserto = consertoService.getConsertoById(id);

        if (!conserto.isPresent()) {
            return new ResponseEntity("Conserto não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            consertoService.excluir(conserto.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}