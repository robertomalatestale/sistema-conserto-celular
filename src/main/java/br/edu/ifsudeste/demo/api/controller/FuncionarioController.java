package br.edu.ifsudeste.demo.api.controller;


import br.edu.ifsudeste.demo.api.dto.FuncionarioDTO;
import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Funcionario;
import br.edu.ifsudeste.demo.model.entity.TipoProduto;
import br.edu.ifsudeste.demo.model.service.FuncionarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/funcionarios")
@RequiredArgsConstructor
@Api
public class FuncionarioController {
    private final FuncionarioService funcionarioService;

    @GetMapping()
    public ResponseEntity get() {
        List<Funcionario> funcionarios = funcionarioService.getFuncionario();
        return ResponseEntity.ok(funcionarios.stream().map(FuncionarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um Funcionário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Funcionário encontrado"),
            @ApiResponse(code = 404, message = "Funcionário não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(id);
        if (!funcionario.isPresent()) {
            return new ResponseEntity("Funcionario não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(funcionario.map(FuncionarioDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva um novo Funcionário")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Funcionário salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o Funcionário")
    })
    public ResponseEntity post(@RequestBody FuncionarioDTO dto) {
        try {
            Funcionario funcionario = converter(dto);
            funcionario = funcionarioService.salvar(funcionario);
            return new ResponseEntity(funcionario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Funcionario converter(FuncionarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Funcionario funcionario = modelMapper.map(dto, Funcionario.class);
        return  funcionario;
    }
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody FuncionarioDTO dto) {
        if (!funcionarioService.getFuncionarioById(id).isPresent()) {
            return new ResponseEntity("Funcionario não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Funcionario funcionario = converter(dto);
            funcionario.setId(id);
            funcionarioService.salvar(funcionario);
            return ResponseEntity.ok(funcionario);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(id);
        if (!funcionario.isPresent()) {
            return new ResponseEntity("Funcionário não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            funcionarioService.excluir(funcionario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

