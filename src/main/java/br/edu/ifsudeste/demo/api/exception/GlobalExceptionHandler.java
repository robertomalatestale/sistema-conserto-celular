package br.edu.ifsudeste.demo.api.exception;

import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<String> handleRegraNegocioException(RegraNegocioException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "Erro ao excluir: Esta entidade está sendo utilizada em outros registros.";

        if (ex.getMessage() != null) {
            if (ex.getMessage().toLowerCase().contains("modelo")) {
                message = "Erro ao excluir: Esta marca está sendo utilizada em um ou mais modelos.";
            }
        }

        return ResponseEntity.badRequest().body(message);
    }
}
