package br.edu.ifsudeste.demo.api.controller;

import br.edu.ifsudeste.demo.api.dto.DispositivoDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Cliente;
import br.edu.ifsudeste.demo.model.entity.Dispositivo;
import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.entity.Modelo;
import br.edu.ifsudeste.demo.model.service.ClienteService;
import br.edu.ifsudeste.demo.model.service.DispositivoService;
import br.edu.ifsudeste.demo.model.service.MarcaService;
import br.edu.ifsudeste.demo.model.service.ModeloService;

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
@RequestMapping("/api/v1/dispositivos")
@RequiredArgsConstructor
@Tag(
        name = "Dispositivos",
        description = "Operações relacionadas ao gerenciamento de dispositivos"
)
public class DispositivoController {

    private final DispositivoService dispositivoService;
    private final ClienteService clienteService;
    private final MarcaService marcaService;
    private final ModeloService modeloService;

    @Operation(
            summary = "Listar dispositivos",
            description = "Retorna todos os dispositivos cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Dispositivos listados com sucesso")
    @GetMapping()
    public ResponseEntity get() {

        List<Dispositivo> dispositivos = dispositivoService.getDispositivo();

        return ResponseEntity.ok(
                dispositivos.stream()
                        .map(DispositivoDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "Buscar dispositivo por ID",
            description = "Retorna os dados de um dispositivo específico."
    )
    @ApiResponse(responseCode = "200", description = "Dispositivo encontrado")
    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {

        Optional<Dispositivo> dispositivo = dispositivoService.getDispositivoById(id);

        if (!dispositivo.isPresent()) {
            return new ResponseEntity("Dispositivo não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(dispositivo.map(DispositivoDTO::create));
    }

    @Operation(
            summary = "Cadastrar dispositivo",
            description = "Realiza o cadastro de um novo dispositivo."
    )
    @ApiResponse(responseCode = "201", description = "Dispositivo cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PostMapping()
    public ResponseEntity post(@RequestBody DispositivoDTO dto) {

        try {
            Dispositivo dispositivo = converter(dto);
            dispositivo = dispositivoService.salvar(dispositivo);

            return new ResponseEntity(dispositivo, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Dispositivo converter(DispositivoDTO dto) {

        ModelMapper modelMapper = new ModelMapper();
        Dispositivo dispositivo = modelMapper.map(dto, Dispositivo.class);

        if (dto.getIdCliente() != null) {

            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());

            if (!cliente.isPresent()) {
                dispositivo.setCliente(null);
            } else {
                dispositivo.setCliente(cliente.get());
            }
        }

        if (dto.getIdMarca() != null) {

            Optional<Marca> marca = marcaService.getMarcaById(dto.getIdMarca());

            if (!marca.isPresent()) {
                dispositivo.setMarca(null);
            } else {
                dispositivo.setMarca(marca.get());
            }
        }

        if (dto.getIdModelo() != null) {

            Optional<Modelo> modelo = modeloService.getModeloById(dto.getIdModelo());

            if (!modelo.isPresent()) {
                dispositivo.setModelo(null);
            } else {
                dispositivo.setModelo(modelo.get());
            }
        }

        return dispositivo;
    }

    @Operation(
            summary = "Atualizar dispositivo",
            description = "Atualiza os dados de um dispositivo já cadastrado."
    )
    @ApiResponse(responseCode = "200", description = "Dispositivo atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody DispositivoDTO dto) {

        if (!dispositivoService.getDispositivoById(id).isPresent()) {
            return new ResponseEntity("Dispositivo não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            Dispositivo dispositivo = converter(dto);
            dispositivo.setId(id);

            dispositivoService.salvar(dispositivo);

            return ResponseEntity.ok(dispositivo);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Excluir dispositivo",
            description = "Remove um dispositivo do sistema."
    )
    @ApiResponse(responseCode = "204", description = "Dispositivo removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Dispositivo> dispositivo = dispositivoService.getDispositivoById(id);

        if (!dispositivo.isPresent()) {
            return new ResponseEntity("Dispositivo não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            dispositivoService.excluir(dispositivo.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}