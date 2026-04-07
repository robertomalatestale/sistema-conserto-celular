package br.edu.ifsudeste.demo.api.dto;

import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.entity.TipoProduto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoProdutoDTO {

    private Long id;
    private String nomeTipo;

    public static TipoProdutoDTO create(TipoProduto tipoProduto) {
        ModelMapper modelMapper = new ModelMapper();
        TipoProdutoDTO dto = modelMapper.map(tipoProduto, TipoProdutoDTO.class);
        return dto;
    }
}