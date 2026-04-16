package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Produto;
import br.edu.ifsudeste.demo.model.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProdutoService {

    private ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> getProduto() {
        return repository.findAll();
    }

    public Optional<Produto> getProdutoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        validar(produto);
        return repository.save(produto);
    }

    @Transactional
    public void excluir(Produto produto) {
        Objects.requireNonNull(produto.getId());
        repository.delete(produto);
    }

    public void validar(Produto produto){
        if (produto.getNome() == null || produto.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome de produto inválido");
        }
        if (produto.getPreco() == null || produto.getPreco() == 0) {
            throw new RegraNegocioException("Preço inválido");
        }
        if (produto.getMarca() == null ||  produto.getMarca().getId() == null || produto.getMarca().getId() == 0) {
            throw new RegraNegocioException("Marca inválida");
        }
        if (produto.getTipoProduto() == null ||  produto.getTipoProduto().getId() == null || produto.getTipoProduto().getId() == 0) {
            throw new RegraNegocioException("Tipo de produto inválido");
        }
        if (produto.getCor() == null || produto.getCor().trim().equals("")) {
            throw new RegraNegocioException("Cor inválida");
        }
        if (produto.getQuantidade() < 0 ) {
            throw new RegraNegocioException("Quantidade inválida");
        }
    }
}
