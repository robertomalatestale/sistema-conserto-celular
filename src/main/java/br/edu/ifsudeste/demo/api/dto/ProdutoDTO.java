package br.edu.ifsudeste.demo.api.dto;

import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.entity.Produto;
import br.edu.ifsudeste.demo.model.entity.TipoProduto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {

    private Long id;
    private String nome;
    private Double preco;
    private Marca marca;
    private Long idTipoProduto;
    private String cor;
    private int quantidade;

    public static ProdutoDTO create(Produto produto) {
        ModelMapper modelMapper = new ModelMapper();
        ProdutoDTO dto = modelMapper.map(produto, ProdutoDTO.class);
        return dto;
    }
}