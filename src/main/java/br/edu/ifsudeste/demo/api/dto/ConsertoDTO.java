package br.edu.ifsudeste.demo.api.dto;

import br.edu.ifsudeste.demo.model.entity.Cliente;
import br.edu.ifsudeste.demo.model.entity.Dispositivo;
import br.edu.ifsudeste.demo.model.entity.Funcionario;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsertoDTO {

    private Long id;
    private Long idCliente;
    private Long idDispositivo;
    private Long idFuncionario;
    private String observacoes;
    private Double valor;
    private Date dataEsperada;
}
