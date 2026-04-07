package br.edu.ifsudeste.demo.api.dto;

import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.entity.Modelo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloDTO {

    private Long id;
    private Marca marca;
    private String nomeModelo;

    public static ModeloDTO create(Modelo modelo) {
        ModelMapper modelMapper = new ModelMapper();
        ModeloDTO dto = modelMapper.map(modelo, ModeloDTO.class);
        return dto;
    }
}