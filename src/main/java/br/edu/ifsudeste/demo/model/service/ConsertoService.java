package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Conserto;
import br.edu.ifsudeste.demo.model.repository.ConsertoRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConsertoService {

    private final ConsertoRepository repository;

    public ConsertoService(ConsertoRepository repository) {
        this.repository = repository;
    }

    public List<Conserto> getConserto() {
        return repository.findAll();
    }

    public Optional<Conserto> getConsertoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Conserto salvar(Conserto conserto) {
        validar(conserto);
        return repository.save(conserto);
    }

    @Transactional
    public void excluir(Conserto conserto) {
        Objects.requireNonNull(conserto.getId());
        repository.delete(conserto);
    }

    public void validar(Conserto conserto) {
        if (conserto.getObservacoes() == null || conserto.getObservacoes().trim().equals("")) {
            throw new RegraNegocioException("Observações inválidas");
        }

        if (conserto.getValor() == null || conserto.getValor() == 0) {
            throw new RegraNegocioException("Valor inválido");
        }
        if (conserto.getDataEsperada() == null /*|| conserto.getDataEsperada() == '2026-04-14 08:20:00'*/) {
            throw new RegraNegocioException("Carga Horária Máxima inválida");
        }
    }
    /*private Date dataEsperada;*/
}
