package br.com.pnipapi.service;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.repository.EnderecoRepository;
import br.com.pnipapi.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UnidadeService {
    UnidadeRepository unidadeRepository;
    EnderecoRepository enderecoRepository;

    public UnidadeService(UnidadeRepository unidadeRepository, EnderecoRepository enderecoRepository) {
        this.unidadeRepository = unidadeRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public ResponseDTO<Unidade> save(UnidadeFormDTO unidade){
        Unidade unidadeSalva = new Unidade();
        if(unidade.idUnidadeGerenciadora() > 0){
            unidadeSalva.setUnidadeGerenciadora(unidadeRepository.getById(unidade.idUnidadeGerenciadora()));
        }
        unidadeSalva = unidadeSalva.toUnidade(unidade);
        if (unidadeSalva.getEndereco() != null) {
            enderecoRepository.save(unidadeSalva.getEndereco());
        }
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
