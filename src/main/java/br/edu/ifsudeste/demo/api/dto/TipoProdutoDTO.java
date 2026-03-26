package br.edu.ifsudeste.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoProdutoDTO {

    private Long id;
    private String nomeTipo;
}
