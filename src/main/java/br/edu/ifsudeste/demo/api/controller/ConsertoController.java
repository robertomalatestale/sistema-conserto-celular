package br.edu.ifsudeste.demo.api.controller;


import br.edu.ifsudeste.demo.api.dto.ConsertoDTO;
import br.edu.ifsudeste.demo.model.entity.Conserto;
import br.edu.ifsudeste.demo.model.service.ConsertoService;
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
@RequestMapping("/api/v1/consertos")
@RequiredArgsConstructor
public class ConsertoController {
    private final ConsertoService consertoService;

    @GetMapping()
    public ResponseEntity get() {
        List<Conserto> consertos = consertoService.getConserto();
        return ResponseEntity.ok(consertos.stream().map(ConsertoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Conserto> conserto = consertoService.getConsertoById(id);
        if (!conserto.isPresent()) {
            return new ResponseEntity("Conserto não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(conserto.map(ConsertoDTO::create));
    }
}
