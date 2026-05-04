package br.edu.ifsudeste.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Double preco;
    @ManyToOne
    private Marca marca;
    @ManyToOne
    private TipoProduto tipoProduto;
    private String cor;
    private int quantidade;
}
