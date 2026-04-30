package br.edu.ifsudeste.demo.api.controller;


import br.edu.ifsudeste.demo.api.dto.ProdutoDTO;
import br.edu.ifsudeste.demo.api.dto.TipoProdutoDTO;
import br.edu.ifsudeste.demo.model.entity.Produto;
import br.edu.ifsudeste.demo.model.entity.TipoProduto;
import br.edu.ifsudeste.demo.model.service.ProdutoService;
import br.edu.ifsudeste.demo.model.service.TipoProdutoService;
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
@RequestMapping("/api/v1/tipoProdutos")
@RequiredArgsConstructor
public class TipoProdutoController {
    private final TipoProdutoService tipoProdutoService;

    @GetMapping()
    public ResponseEntity get() {
        List<TipoProduto> tipoProdutos = tipoProdutoService.getTipoProduto();
        return ResponseEntity.ok(tipoProdutos.stream().map(TipoProdutoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<TipoProduto> tipoProduto = tipoProdutoService.getTipoProdutoById(id);
        if (!tipoProduto.isPresent()) {
            return new ResponseEntity("Tipo de Produto não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tipoProduto.map(TipoProdutoDTO::create));
    }
}
