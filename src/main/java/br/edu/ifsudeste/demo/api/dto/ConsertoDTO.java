package br.edu.ifsudeste.demo.api.dto;

import br.edu.ifsudeste.demo.model.entity.Conserto;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

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

    public static ConsertoDTO create(Conserto conserto) {
        ModelMapper modelMapper = new ModelMapper();
        ConsertoDTO dto = modelMapper.map(conserto, ConsertoDTO.class);
        return dto;
    }
}