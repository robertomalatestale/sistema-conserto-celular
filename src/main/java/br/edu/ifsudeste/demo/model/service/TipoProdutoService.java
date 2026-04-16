package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Cliente;
import br.edu.ifsudeste.demo.model.entity.TipoProduto;
import br.edu.ifsudeste.demo.model.repository.TipoProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TipoProdutoService {

    private final TipoProdutoRepository repository;

    public TipoProdutoService(TipoProdutoRepository repository) {
        this.repository = repository;
    }

    public List<TipoProduto> getTipoProduto() {
        return repository.findAll();
    }

    public Optional<TipoProduto> getTipoProdutoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public TipoProduto salvar(TipoProduto tipoProduto) {
        validar(tipoProduto);
        return repository.save(tipoProduto);
    }

    @Transactional
    public void excluir(TipoProduto tipoProduto) {
        Objects.requireNonNull(tipoProduto.getId());
        repository.delete(tipoProduto);
    }

    public void validar(TipoProduto tipoProduto) {
        if (tipoProduto.getNomeTipo() == null || tipoProduto.getNomeTipo().trim().equals("")) {
            throw new RegraNegocioException("Nome do tipo inválido");
        }
    }
}
