package br.edu.ifsudeste.demo.api.dto;

import br.edu.ifsudeste.demo.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispositivoDTO {

    private Long id;
    private Long idCliente;
    private int ano;
    private Long idMarca;
    private Long idModelo;

    public static DispositivoDTO create(Dispositivo dispositivo) {
        ModelMapper modelMapper = new ModelMapper();
        DispositivoDTO dto = modelMapper.map(dispositivo, DispositivoDTO.class);
        return dto;
    }
}