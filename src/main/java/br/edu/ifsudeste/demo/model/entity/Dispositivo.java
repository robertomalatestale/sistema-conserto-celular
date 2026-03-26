package br.edu.ifsudeste.demo.model.entity;

import jakarta.persistence.*;

@Entity
public class Dispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne
    private Cliente cliente;

    private int ano;
    private Marca marca;
    private Modelo modelo;
}
