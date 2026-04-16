package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Modelo;
import br.edu.ifsudeste.demo.model.repository.ModeloRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ModeloService {

    private final ModeloRepository repository;

    public ModeloService(ModeloRepository repository) {
        this.repository = repository;
    }

    public List<Modelo> getModelo() {
        return repository.findAll();
    }

    public Optional<Modelo> getModeloById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Modelo salvar(Modelo modelo) {
        validar(modelo);
        return repository.save(modelo);
    }

    @Transactional
    public void excluir(Modelo modelo) {
        Objects.requireNonNull(modelo.getId());
        repository.delete(modelo);
    }

    public void validar(Modelo modelo) {
        if (modelo.getNomeModelo() == null || modelo.getNomeModelo().trim().equals("")) {
            throw new RegraNegocioException("Nome do modelo inválido");
        }
        if (modelo.getMarca() == null || modelo.getMarca().getId() == null || modelo.getMarca().getId() == 0) {
            throw new RegraNegocioException("Marca inválida");
        }


    }
}
