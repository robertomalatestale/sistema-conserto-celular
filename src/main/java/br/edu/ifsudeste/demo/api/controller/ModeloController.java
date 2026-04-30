package br.edu.ifsudeste.demo.api.controller;


import br.edu.ifsudeste.demo.api.dto.ModeloDTO;
import br.edu.ifsudeste.demo.model.entity.Modelo;
import br.edu.ifsudeste.demo.model.service.ModeloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/modelos")
@RequiredArgsConstructor
public class ModeloController {
    private final ModeloService modeloService;

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
}
