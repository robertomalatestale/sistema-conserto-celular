package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Funcionario;
import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.repository.MarcaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MarcaService {

    private final MarcaRepository repository;

    public MarcaService(MarcaRepository repository) {
        this.repository = repository;
    }

    public List<Marca> getMarca() {
        return repository.findAll();
    }

    public Optional<Marca> getMarcaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Marca salvar(Marca marca) {
        validar(marca);
        return repository.save(marca);
    }

    @Transactional
    public void excluir(Marca marca) {
        Objects.requireNonNull(marca.getId());
        repository.delete(marca);
    }

    public void validar(Marca marca) {
        if (marca.getNomeMarca() == null || marca.getNomeMarca().trim().equals("")) {
            throw new RegraNegocioException("Nome da marca inválido");
        }

    }
}
