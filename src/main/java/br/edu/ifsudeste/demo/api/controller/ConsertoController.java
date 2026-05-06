package br.edu.ifsudeste.demo.api.controller;


import br.edu.ifsudeste.demo.api.dto.ClienteDTO;
import br.edu.ifsudeste.demo.api.dto.ConsertoDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.*;
import br.edu.ifsudeste.demo.model.service.ClienteService;
import br.edu.ifsudeste.demo.model.service.ConsertoService;
import br.edu.ifsudeste.demo.model.service.DispositivoService;
import br.edu.ifsudeste.demo.model.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/consertos")
@RequiredArgsConstructor
public class ConsertoController {
    private final ConsertoService consertoService;
    private final ClienteService clienteService;
    private final DispositivoService dispositivoService;
    private final FuncionarioService funcionarioService;

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

    @PostMapping()
    public ResponseEntity post(@RequestBody ConsertoDTO dto) {
        try {
            Conserto conserto = converter(dto);
            conserto = consertoService.salvar(conserto);
            return new ResponseEntity(conserto, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Conserto converter(ConsertoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Conserto conserto = modelMapper.map(dto, Conserto.class);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                conserto.setCliente(null);
            } else {
                conserto.setCliente(cliente.get());
            }
        }
        if (dto.getIdDispositivo() != null) {
            Optional<Dispositivo> dispositivo = dispositivoService.getDispositivoById(dto.getIdDispositivo());
            if (!dispositivo.isPresent()) {
                conserto.setDispositivo(null);
            } else {
                conserto.setDispositivo(dispositivo.get());
            }
        }
        if (dto.getIdFuncionario() != null) {
            Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(dto.getIdFuncionario());
            if (!funcionario.isPresent()) {
                conserto.setFuncionario(null);
            } else {
                conserto.setFuncionario(funcionario.get());
            }
        }
        return  conserto;
    }
}
