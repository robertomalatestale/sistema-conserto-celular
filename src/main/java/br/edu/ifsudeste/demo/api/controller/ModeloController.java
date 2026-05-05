package br.edu.ifsudeste.demo.api.controller;


import br.edu.ifsudeste.demo.api.dto.ModeloDTO;
import br.edu.ifsudeste.demo.api.dto.TipoProdutoDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.entity.Modelo;
import br.edu.ifsudeste.demo.model.entity.TipoProduto;
import br.edu.ifsudeste.demo.model.service.MarcaService;
import br.edu.ifsudeste.demo.model.service.ModeloService;
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
public class ModeloController {
    private final ModeloService modeloService;
    private final MarcaService marcaService;

    @GetMapping()
    public ResponseEntity get() {
        List<Modelo> modelos = modeloService.getModelo();
        return ResponseEntity.ok(modelos.stream().map(ModeloDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Modelo> modelo = modeloService.getModeloById(id);
        if (!modelo.isPresent()) {
            return new ResponseEntity("Modelo não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(modelo.map(ModeloDTO::create));
    }

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
        return  modelo;
    }
}
