package br.edu.ifsudeste.demo.model.repository;

import br.edu.ifsudeste.demo.model.entity.Cliente;
import br.edu.ifsudeste.demo.model.entity.Conserto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsertoRepository extends JpaRepository<Conserto, Long> {
}
