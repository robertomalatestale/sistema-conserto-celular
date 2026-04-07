package br.edu.ifsudeste.demo.model.repository;

import br.edu.ifsudeste.demo.model.entity.Cliente;
import br.edu.ifsudeste.demo.model.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
}
