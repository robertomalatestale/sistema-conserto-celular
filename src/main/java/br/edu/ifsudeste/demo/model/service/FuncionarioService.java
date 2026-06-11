package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Funcionario;
import br.edu.ifsudeste.demo.model.repository.FuncionarioRepository;
import br.edu.ifsudeste.demo.util.ValidadorCPFEmail;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FuncionarioService {

    private FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public List<Funcionario> getFuncionario() {
        return repository.findAll();
    }

    public Optional<Funcionario> getFuncionarioById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        validar(funcionario);
        return repository.save(funcionario);
    }

    @Transactional
    public void excluir(Funcionario funcionario) {
        Objects.requireNonNull(funcionario.getId());
        repository.delete(funcionario);
    }

    public void validar(Funcionario funcionario) {
        if (funcionario.getNomeCompleto() == null || funcionario.getNomeCompleto().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        try {
            ValidadorCPFEmail.validarCPF(funcionario.getCpf());
        } catch (IllegalArgumentException e) {
            throw new RegraNegocioException(e.getMessage());
        }

        if (funcionario.getTelefoneCelular() == null || funcionario.getTelefoneCelular().trim().equals("")) {
            throw new RegraNegocioException("Celular inválido");
        }

        try {
            ValidadorCPFEmail.validarEmail(funcionario.getEmail());
        } catch (IllegalArgumentException e) {
            throw new RegraNegocioException(e.getMessage());
        }
    }
}

