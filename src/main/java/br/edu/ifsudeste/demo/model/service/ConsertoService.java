package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.model.entity.Conserto;
import br.edu.ifsudeste.demo.model.repository.ConsertoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsertoService {

    private ConsertoRepository repository;

    public ConsertoService(ConsertoRepository repository) {
        this.repository = repository;
    }

    public List<Conserto> getConserto() {
        return repository.findAll();
    }

    public Optional<Conserto> getConsertoById(Long id) {
        return repository.findById(id);
    }
}
