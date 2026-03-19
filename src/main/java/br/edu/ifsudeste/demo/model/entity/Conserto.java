package br.edu.ifsudeste.demo.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class Conserto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Dispositivo dispositivo;

    @ManyToOne
    private Funcionario funcionario;

    private String observacoes;
    private Double valor;
    private Date dataEsperada;
}
