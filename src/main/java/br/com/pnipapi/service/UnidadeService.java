package br.com.pnipapi.service;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.model.Endereco;
import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class UnidadeService {
    UnidadeRepository unidadeRepository;

    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    public ResponseDTO<Unidade> save(UnidadeFormDTO unidade){
        Unidade unidadeSalva = new Unidade();
        if(unidade.idUnidadeGerenciadora() > 0){
            unidadeSalva.setUnidadeGerenciadora(unidadeRepository.getById(unidade.idUnidadeGerenciadora()));
        }
        unidadeSalva.toUnidade(unidade);
        unidadeRepository.save(unidadeSalva);
        return ResponseDTO.ok( "Unidade cadastrada com sucesso!", unidadeSalva);
    }

    public List<Unidade> findAll(){
        return unidadeRepository.findAll().parallelStream().filter(Objects::nonNull).toList();
    }

    public List<Unidade> getGerenciadoras(String tipo){
        return unidadeRepository.getUnidadeByTipo(tipo);
    }

}
