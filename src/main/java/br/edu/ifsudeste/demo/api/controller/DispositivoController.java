package br.edu.ifsudeste.demo.api.controller;


import br.edu.ifsudeste.demo.api.dto.DispositivoDTO;
import br.edu.ifsudeste.demo.model.entity.Dispositivo;
import br.edu.ifsudeste.demo.model.service.DispositivoService;
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
@RequestMapping("/api/v1/dispositivos")
@RequiredArgsConstructor
public class DispositivoController {
    private final DispositivoService dispositivoService;

    @GetMapping()
    public ResponseEntity get() {
        List<Dispositivo> dispositivos = dispositivoService.getDispositivo();
        return ResponseEntity.ok(dispositivos.stream().map(DispositivoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Dispositivo> dispositivo = dispositivoService.getDispositivoById(id);
        if (!dispositivo.isPresent()) {
            return new ResponseEntity("Dispositivo não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(dispositivo.map(DispositivoDTO::create));
    }
}
