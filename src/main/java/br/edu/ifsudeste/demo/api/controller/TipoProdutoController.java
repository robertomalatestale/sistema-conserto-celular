package br.edu.ifsudeste.demo.api.controller;

import br.edu.ifsudeste.demo.api.dto.TipoProdutoDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.TipoProduto;
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
@RequestMapping("/api/v1/tipoProdutos")
@RequiredArgsConstructor
@Tag(
        name = "Tipos de Produto",
        description = "Operações relacionadas ao gerenciamento dos tipos de produtos"
)
public class TipoProdutoController {

    private final TipoProdutoService tipoProdutoService;

    @Operation(
            summary = "Listar tipos de produto",
            description = "Retorna todos os tipos de produto cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Tipos de produto listados com sucesso")
    @GetMapping()
    public ResponseEntity get() {
        List<TipoProduto> tipoProdutos = tipoProdutoService.getTipoProduto();

        return ResponseEntity.ok(
                tipoProdutos.stream()
                        .map(TipoProdutoDTO::create)
                        .collect(Collectors.toList())
        );
    }

    @Operation(
            summary = "Buscar tipo de produto por ID",
            description = "Retorna os dados de um tipo de produto específico."
    )
    @ApiResponse(responseCode = "200", description = "Tipo de produto encontrado")
    @ApiResponse(responseCode = "404", description = "Tipo de produto não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {

        Optional<TipoProduto> tipoProduto = tipoProdutoService.getTipoProdutoById(id);

        if (!tipoProduto.isPresent()) {
            return new ResponseEntity("Tipo de Produto não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(tipoProduto.map(TipoProdutoDTO::create));
    }

    @Operation(
            summary = "Cadastrar tipo de produto",
            description = "Realiza o cadastro de um novo tipo de produto."
    )
    @ApiResponse(responseCode = "201", description = "Tipo de produto cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PostMapping()
    public ResponseEntity post(@RequestBody TipoProdutoDTO dto) {

        try {
            TipoProduto tipoProduto = converter(dto);
            tipoProduto = tipoProdutoService.salvar(tipoProduto);

            return new ResponseEntity(tipoProduto, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public TipoProduto converter(TipoProdutoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, TipoProduto.class);
    }

    @Operation(
            summary = "Atualizar tipo de produto",
            description = "Atualiza os dados de um tipo de produto já cadastrado."
    )
    @ApiResponse(responseCode = "200", description = "Tipo de produto atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Tipo de produto não encontrado")
    @ApiResponse(responseCode = "400", description = "Erro de validação dos dados")
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,
                                    @RequestBody TipoProdutoDTO dto) {

        if (!tipoProdutoService.getTipoProdutoById(id).isPresent()) {
            return new ResponseEntity("Tipo de Produto não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            TipoProduto tipoProduto = converter(dto);
            tipoProduto.setId(id);

            tipoProdutoService.salvar(tipoProduto);

            return ResponseEntity.ok(tipoProduto);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Excluir tipo de produto",
            description = "Remove um tipo de produto do sistema."
    )
    @ApiResponse(responseCode = "204", description = "Tipo de produto removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Tipo de produto não encontrado")
    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {

        Optional<TipoProduto> tipoProduto = tipoProdutoService.getTipoProdutoById(id);

        if (!tipoProduto.isPresent()) {
            return new ResponseEntity("Tipo de produto não encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            tipoProdutoService.excluir(tipoProduto.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}