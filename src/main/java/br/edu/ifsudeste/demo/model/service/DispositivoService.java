package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.*;
import br.edu.ifsudeste.demo.model.entity.Dispositivo;
import br.edu.ifsudeste.demo.model.repository.DispositivoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Dispositivo salvar(Dispositivo dispositivo) {
        validar(dispositivo);
        return repository.save(dispositivo);
    }

    @Transactional
    public void excluir(Dispositivo dispositivo) {
        Objects.requireNonNull(dispositivo.getId());
        repository.delete(dispositivo);
    }

    public void validar(Dispositivo dispositivo) {
        if (dispositivo.getCliente() == null || dispositivo.getCliente().getId() == null || dispositivo.getCliente().getId() == 0) {
            throw new RegraNegocioException("Cliente inválido");
        }
        if (dispositivo.getAno() <= 0) {
            throw new RegraNegocioException("Ano inválido");
        }
        if (dispositivo.getMarca() == null ||  dispositivo.getMarca().getId() == null || dispositivo.getMarca().getId() == 0) {
            throw new RegraNegocioException("Marca inválida");
        }
        if (dispositivo.getModelo() == null || dispositivo.getModelo().getId() == null || dispositivo.getModelo().getId() == 0) {
            throw new RegraNegocioException("Modelo inválido");
        }
    }

}

