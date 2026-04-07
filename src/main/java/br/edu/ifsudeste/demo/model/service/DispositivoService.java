package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.model.entity.Dispositivo;
import br.edu.ifsudeste.demo.model.repository.DispositivoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DispositivoService {

    private DispositivoRepository repository;

    public DispositivoService(DispositivoRepository repository) {
        this.repository = repository;
    }

    public List<Dispositivo> getDispositivo() {
        return repository.findAll();
    }

    public Optional<Dispositivo> getDispositivoById(Long id) {
        return repository.findById(id);
    }
}
