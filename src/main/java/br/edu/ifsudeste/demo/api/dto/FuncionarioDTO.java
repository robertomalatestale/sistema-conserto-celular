package br.edu.ifsudeste.demo.api.dto;

import br.edu.ifsudeste.demo.model.entity.Dispositivo;
import br.edu.ifsudeste.demo.model.entity.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {

    private Long id;
    private String nomeCompleto;
    private String cpf;
    private String telefoneCelular;
    private String email;

    public static FuncionarioDTO create(Funcionario funcionario) {
        ModelMapper modelMapper = new ModelMapper();
        FuncionarioDTO dto = modelMapper.map(funcionario, FuncionarioDTO.class);
        return dto;
    }
}