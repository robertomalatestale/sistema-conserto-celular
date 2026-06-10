package br.edu.ifsudeste.demo.api.controller;

import br.edu.ifsudeste.demo.api.dto.ModeloDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.entity.Modelo;
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
@RequestMapping("/api/v1/modelos")
@RequiredArgsConstructor
@Tag(
        name = "Modelos",
        description = "Operações relacionadas ao gerenciamento de modelos"
)
public class ModeloController {

    private final ModeloService modeloService;
    private final MarcaService marcaService;

    @Operation(
            summary = "Listar modelos",
            description = "Retorna todos os modelos cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Modelos listados com sucesso")
    @GetMapping()
    public ResponseEntity get() {
        List<Modelo> modelos = modeloService.getModelo();

        return ResponseEntity.ok(
                modelos.stream()
                        .map(ModeloDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "Buscar modelo por ID",
            description = "Retorna os dados de um modelo específico."
    )
    @ApiResponse(responseCode = "200", description = "Modelo encontrado")
    @ApiResponse(responseCode = "404", description = "Modelo não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {

        Optional<Modelo> modelo = modeloService.getModeloById(id);

        if (!modelo.isPresent()) {
            return new ResponseEntity("Modelo não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(modelo.map(ModeloDTO::create));
    }

    @Operation(
            summary = "Cadastrar modelo",
            description = "Realiza o cadastro de um novo modelo."
    )
    @ApiResponse(responseCode = "201", description = "Modelo cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PostMapping()
    public ResponseEntity post(@RequestBody ModeloDTO dto) {

        try {
            Modelo modelo = converter(dto);
            modelo = modeloService.salvar(modelo);

            return new ResponseEntity(modelo, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Modelo converter(ModeloDTO dto) {

        ModelMapper modelMapper = new ModelMapper();
        Modelo modelo = modelMapper.map(dto, Modelo.class);

        if (dto.getIdMarca() != null) {

            Optional<Marca> marca = marcaService.getMarcaById(dto.getIdMarca());

            if (!marca.isPresent()) {
                modelo.setMarca(null);
            } else {
                modelo.setMarca(marca.get());
            }
        }

        return modelo;
    }

    @Operation(
            summary = "Atualizar modelo",
            description = "Atualiza os dados de um modelo já cadastrado."
    )
    @ApiResponse(responseCode = "200", description = "Modelo atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Modelo não encontrado")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody ModeloDTO dto) {

        if (!modeloService.getModeloById(id).isPresent()) {
            return new ResponseEntity("Modelo não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            Modelo modelo = converter(dto);
            modelo.setId(id);

            modeloService.salvar(modelo);

            return ResponseEntity.ok(modelo);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Excluir modelo",
            description = "Remove um modelo do sistema."
    )
    @ApiResponse(responseCode = "204", description = "Modelo removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Modelo não encontrado")
    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Modelo> modelo = modeloService.getModeloById(id);

        if (!modelo.isPresent()) {
            return new ResponseEntity("Modelo não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            modeloService.excluir(modelo.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}