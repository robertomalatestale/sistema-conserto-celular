package br.edu.ifsudeste.demo.model.repository;

import br.edu.ifsudeste.demo.model.entity.Cliente;
import br.edu.ifsudeste.demo.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
