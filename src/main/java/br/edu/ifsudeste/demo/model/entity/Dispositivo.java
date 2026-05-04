package br.edu.ifsudeste.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne
    private Cliente cliente;

    private int ano;
    @ManyToOne
    private Marca marca;
    @ManyToOne
    private Modelo modelo;
}
