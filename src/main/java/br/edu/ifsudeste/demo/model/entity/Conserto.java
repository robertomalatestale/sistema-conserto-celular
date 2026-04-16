package br.edu.ifsudeste.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Conserto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
