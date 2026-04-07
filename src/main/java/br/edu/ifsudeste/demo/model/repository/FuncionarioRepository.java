package br.edu.ifsudeste.demo.model.repository;

import br.edu.ifsudeste.demo.model.entity.Cliente;
import br.edu.ifsudeste.demo.model.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
