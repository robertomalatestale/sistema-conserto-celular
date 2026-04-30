package br.edu.ifsudeste.demo.api.controller;


import br.edu.ifsudeste.demo.api.dto.MarcaDTO;
import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.service.MarcaService;
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
@RequestMapping("/api/v1/marcas")
@RequiredArgsConstructor
public class MarcaController {
    private final MarcaService marcaService;

    @GetMapping()
    public ResponseEntity get() {
        List<Marca> marcas = marcaService.getMarca();
        return ResponseEntity.ok(marcas.stream().map(MarcaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Marca> marca = marcaService.getMarcaById(id);
        if (!marca.isPresent()) {
            return new ResponseEntity("Marca não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(marca.map(MarcaDTO::create));
    }
}
