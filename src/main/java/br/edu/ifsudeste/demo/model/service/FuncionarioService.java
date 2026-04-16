package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Funcionario;
import br.edu.ifsudeste.demo.model.entity.Funcionario;
import br.edu.ifsudeste.demo.model.repository.FuncionarioRepository;
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
        if (funcionario.getCpf() == null || funcionario.getCpf().trim().equals("")) {
            throw new RegraNegocioException("CPF inválido");
        }
        if (funcionario.getTelefoneCelular() == null || funcionario.getTelefoneCelular().trim().equals("")) {
            throw new RegraNegocioException("Celular inválido");
        }
        if (funcionario.getEmail() == null || funcionario.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }
    }
}
