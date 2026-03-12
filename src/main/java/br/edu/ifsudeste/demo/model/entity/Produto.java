package br.edu.ifsudeste.demo.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;
    private Double preco;
    private long idMarca;
    private long idTipoProduto;
    private String cor;
    private int quantidade;
}
