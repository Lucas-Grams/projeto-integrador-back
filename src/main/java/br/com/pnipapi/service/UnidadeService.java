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
        unidadeSalva.setUnidadeGerenciadora(unidadeRepository.getById(unidade.idUnidadeGerenciadora()));
        unidadeSalva.setNome(unidade.nome());
        unidadeSalva.setTipo(unidade.tipo());
        unidadeSalva.setDataCadastro(Date.valueOf(LocalDate.now()));

        Endereco endereco = new Endereco();
        endereco.setUf(unidade.uf());
        endereco.setCidade(unidade.cidade());
        endereco.setBairro(unidade.bairro());
        endereco.setRua(unidade.rua());
        endereco.setComplemento(unidade.complemento());
        endereco.setNumero(unidade.numero());
        endereco.setCep(unidade.cep());
        unidadeSalva.setEndereco(endereco);
        System.out.println("service");
        unidadeRepository.save(unidadeSalva);
        return ResponseDTO.ok( "Unidade cadastrada com sucesso!", unidadeSalva);
    }

    public List<Unidade> findAll(){
        return unidadeRepository.findAll().parallelStream().filter(Objects::nonNull).toList();
    }

}
