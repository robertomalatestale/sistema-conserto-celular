package br.edu.ifsudeste.demo.model.repository;

import br.edu.ifsudeste.demo.model.entity.Cliente;
import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    @Query("SELECT m FROM Modelo m WHERE m.marca = :marca")
    List<Modelo> findByMarca(@Param("marca") Marca marca);
}

