package br.edu.ifsudeste.demo.api.controller;


import br.edu.ifsudeste.demo.api.dto.ConsertoDTO;
import br.edu.ifsudeste.demo.api.dto.DispositivoDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.*;
import br.edu.ifsudeste.demo.model.service.ClienteService;
import br.edu.ifsudeste.demo.model.service.DispositivoService;
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
@RequestMapping("/api/v1/dispositivos")
@RequiredArgsConstructor
public class DispositivoController {
    private final DispositivoService dispositivoService;
    private final ClienteService clienteService;
    private final MarcaService marcaService;
    private final ModeloService modeloService;

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
        return  dispositivo;
    }
}
