package br.edu.ifsudeste.demo.api.controller;


import br.edu.ifsudeste.demo.api.dto.MarcaDTO;
import br.edu.ifsudeste.demo.api.dto.TipoProdutoDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.entity.TipoProduto;
import br.edu.ifsudeste.demo.model.service.MarcaService;
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
        Marca marca = modelMapper.map(dto, Marca.class);
        return  marca;
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody MarcaDTO dto) {
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
}
