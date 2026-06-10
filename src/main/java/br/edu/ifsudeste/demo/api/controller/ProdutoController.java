package br.edu.ifsudeste.demo.api.controller;

import br.edu.ifsudeste.demo.api.dto.ProdutoDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.entity.Produto;
import br.edu.ifsudeste.demo.model.entity.TipoProduto;
import br.edu.ifsudeste.demo.model.service.MarcaService;
import br.edu.ifsudeste.demo.model.service.ProdutoService;
import br.edu.ifsudeste.demo.model.service.TipoProdutoService;

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
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
@Tag(
        name = "Produtos",
        description = "Operações relacionadas ao gerenciamento de produtos"
)
public class ProdutoController {

    private final ProdutoService produtoService;
    private final MarcaService marcaService;
    private final TipoProdutoService tipoProdutoService;

    @Operation(
            summary = "Listar produtos",
            description = "Retorna todos os produtos cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Produtos listados com sucesso")
    @GetMapping()
    public ResponseEntity get() {

        List<Produto> produtos = produtoService.getProduto();

        return ResponseEntity.ok(
                produtos.stream()
                        .map(ProdutoDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os dados de um produto específico."
    )
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {

        Optional<Produto> produto = produtoService.getProdutoById(id);

        if (!produto.isPresent()) {
            return new ResponseEntity("Produto não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(produto.map(ProdutoDTO::create));
    }

    @Operation(
            summary = "Cadastrar produto",
            description = "Realiza o cadastro de um novo produto."
    )
    @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PostMapping()
    public ResponseEntity post(@RequestBody ProdutoDTO dto) {

        try {
            Produto produto = converter(dto);
            produto = produtoService.salvar(produto);

            return new ResponseEntity(produto, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Produto converter(ProdutoDTO dto) {

        ModelMapper modelMapper = new ModelMapper();
        Produto produto = modelMapper.map(dto, Produto.class);

        if (dto.getIdMarca() != null) {

            Optional<Marca> marca = marcaService.getMarcaById(dto.getIdMarca());

            if (!marca.isPresent()) {
                produto.setMarca(null);
            } else {
                produto.setMarca(marca.get());
            }
        }

        if (dto.getIdTipoProduto() != null) {

            Optional<TipoProduto> tipoProduto =
                    tipoProdutoService.getTipoProdutoById(dto.getIdTipoProduto());

            if (!tipoProduto.isPresent()) {
                produto.setTipoProduto(null);
            } else {
                produto.setTipoProduto(tipoProduto.get());
            }
        }

        return produto;
    }

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza os dados de um produto já cadastrado."
    )
    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody ProdutoDTO dto) {

        if (!produtoService.getProdutoById(id).isPresent()) {
            return new ResponseEntity("Produto não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            Produto produto = converter(dto);
            produto.setId(id);

            produtoService.salvar(produto);

            return ResponseEntity.ok(produto);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Excluir produto",
            description = "Remove um produto do sistema."
    )
    @ApiResponse(responseCode = "204", description = "Produto removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<Produto> produto = produtoService.getProdutoById(id);

        if (!produto.isPresent()) {
            return new ResponseEntity("Produto não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            produtoService.excluir(produto.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}