package br.edu.ifsudeste.demo.api.dto;

import br.edu.ifsudeste.demo.model.entity.Funcionario;
import br.edu.ifsudeste.demo.model.entity.Marca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarcaDTO {

    private Long id;
    private String nomeMarca;

    public static MarcaDTO create(Marca marca) {
        ModelMapper modelMapper = new ModelMapper();
        MarcaDTO dto = modelMapper.map(marca, MarcaDTO.class);
        return dto;
    }
}