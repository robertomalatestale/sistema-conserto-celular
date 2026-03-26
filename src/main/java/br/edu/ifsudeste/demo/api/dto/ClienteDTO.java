package br.edu.ifsudeste.demo.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;
    private String nomeCompleto;
    private String cpf;
    private String telefoneCelular;
    private String email;
}
