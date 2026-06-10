package br.edu.ifsudeste.demo.api.controller;

import br.edu.ifsudeste.demo.api.dto.ClienteDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Cliente;
import br.edu.ifsudeste.demo.model.service.ClienteService;

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
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(
        name = "Clientes",
        description = "Operações relacionadas ao gerenciamento de clientes"
)
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(
            summary = "Listar clientes",
            description = "Retorna todos os clientes cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Clientes listados com sucesso")
    @GetMapping()
    public ResponseEntity get() {
        List<Cliente> clientes = clienteService.getCliente();
        return ResponseEntity.ok(
                clientes.stream()
                        .map(ClienteDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Retorna os dados de um cliente específico."
    )
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = clienteService.getClienteById(id);

        if (!cliente.isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(cliente.map(ClienteDTO::create));
    }

    @Operation(
            summary = "Cadastrar cliente",
            description = "Realiza o cadastro de um novo cliente."
    )
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PostMapping()
    public ResponseEntity post(@RequestBody ClienteDTO dto) {
        try {
            Cliente cliente = converter(dto);
            cliente = clienteService.salvar(cliente);
            return new ResponseEntity(cliente, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Cliente converter(ClienteDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Cliente.class);
    }

    @Operation(
            summary = "Atualizar cliente",
            description = "Atualiza os dados de um cliente já cadastrado."
    )
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody ClienteDTO dto) {

        if (!clienteService.getClienteById(id).isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            Cliente cliente = converter(dto);
            cliente.setId(id);
            clienteService.salvar(cliente);

            return ResponseEntity.ok(cliente);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Excluir cliente",
            description = "Remove um cliente do sistema."
    )
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Cliente> cliente = clienteService.getClienteById(id);

        if (!cliente.isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            clienteService.excluir(cliente.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}