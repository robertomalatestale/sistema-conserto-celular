package br.edu.ifsudeste.demo.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class Conserto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idCliente;
    private long idDispositivo;
    private long idFuncionario;
    private String observacoes;
    private Double valor;
    private Date dataEsperada;
}
