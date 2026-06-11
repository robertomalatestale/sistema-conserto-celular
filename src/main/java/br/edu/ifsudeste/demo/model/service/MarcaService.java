package br.edu.ifsudeste.demo.model.service;

import br.edu.ifsudeste.demo.exception.RegraNegocioException;
import br.edu.ifsudeste.demo.model.entity.Marca;
import br.edu.ifsudeste.demo.model.entity.Modelo;
import br.edu.ifsudeste.demo.model.repository.MarcaRepository;
import br.edu.ifsudeste.demo.model.repository.ModeloRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarcaService {

    private final MarcaRepository repository;
    private final ModeloRepository modeloRepository;

    public MarcaService(MarcaRepository repository, ModeloRepository modeloRepository) {
        this.repository = repository;
        this.modeloRepository = modeloRepository;
    }

    public List<Marca> getMarca() {
        return repository.findAll();
    }

    public Optional<Marca> getMarcaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Marca salvar(Marca marca) {
        validar(marca);
        return repository.save(marca);
    }

    @Transactional
    public void excluir(Marca marca) {
        Objects.requireNonNull(marca.getId());

        List<Modelo> modelosUtilizando = modeloRepository.findByMarca(marca);
        if (!modelosUtilizando.isEmpty()) {
            String nomesModelos = modelosUtilizando.stream()
                    .map(Modelo::getNomeModelo)
                    .collect(Collectors.joining(", "));
            throw new RegraNegocioException("Falha ao excluir: Marca sendo utilizada no(s) modelo(s) " + nomesModelos);
        }

        repository.delete(marca);
    }

    public void validar(Marca marca) {
        if (marca.getNomeMarca() == null || marca.getNomeMarca().trim().equals("")) {
            throw new RegraNegocioException("Nome da marca inválido");
        }

    }
}

