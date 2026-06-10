package br.edu.ifsudeste.demo.api.controller;

import br.edu.ifsudeste.demo.api.dto.MarcaDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.service.MarcaService;

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
@RequestMapping("/api/v1/marcas")
@RequiredArgsConstructor
@Tag(
        name = "Marcas",
        description = "Operações relacionadas ao gerenciamento de marcas"
)
public class MarcaController {

    private final MarcaService marcaService;

    @Operation(
            summary = "Listar marcas",
            description = "Retorna todas as marcas cadastradas no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Marcas listadas com sucesso")
    @GetMapping()
    public ResponseEntity get() {

        List<Marca> marcas = marcaService.getMarca();

        return ResponseEntity.ok(
                marcas.stream()
                        .map(MarcaDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "Buscar marca por ID",
            description = "Retorna os dados de uma marca específica."
    )
    @ApiResponse(responseCode = "200", description = "Marca encontrada")
    @ApiResponse(responseCode = "404", description = "Marca não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {

        Optional<Marca> marca = marcaService.getMarcaById(id);

        if (!marca.isPresent()) {
            return new ResponseEntity("Marca não encontrada", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(marca.map(MarcaDTO::create));
    }

    @Operation(
            summary = "Cadastrar marca",
            description = "Realiza o cadastro de uma nova marca."
    )
    @ApiResponse(responseCode = "201", description = "Marca cadastrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PostMapping()
    public ResponseEntity post(@RequestBody MarcaDTO dto) {

        try {
            Marca marca = converter(dto);
            marca = marcaService.salvar(marca);

            return new ResponseEntity(marca, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Marca converter(MarcaDTO dto) {

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Marca.class);
    }

    @Operation(
            summary = "Atualizar marca",
            description = "Atualiza os dados de uma marca já cadastrada."
    )
    @ApiResponse(responseCode = "200", description = "Marca atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Marca não encontrada")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody MarcaDTO dto) {

        if (!marcaService.getMarcaById(id).isPresent()) {
            return new ResponseEntity("Marca não encontrada", HttpStatus.NOT_FOUND);
        }

        try {
            Marca marca = converter(dto);
            marca.setId(id);

            marcaService.salvar(marca);

            return ResponseEntity.ok(marca);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Excluir marca",
            description = "Remove uma marca do sistema."
    )
    @ApiResponse(responseCode = "204", description = "Marca removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Marca não encontrada")
    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Marca> marca = marcaService.getMarcaById(id);

        if (!marca.isPresent()) {
            return new ResponseEntity("Marca não encontrada", HttpStatus.NOT_FOUND);
        }

        try {
            marcaService.excluir(marca.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}