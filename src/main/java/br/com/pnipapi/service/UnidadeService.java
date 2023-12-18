package br.com.pnipapi.service;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.dto.UsuarioInfo;
import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.repository.EnderecoRepository;
import br.com.pnipapi.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UnidadeService {
    UnidadeRepository unidadeRepository;
    EnderecoRepository enderecoRepository;
    UsuarioService usuarioService;
    public UnidadeService(UnidadeRepository unidadeRepository, EnderecoRepository enderecoRepository, UsuarioService usuarioService) {
        this.unidadeRepository = unidadeRepository;
        this.enderecoRepository = enderecoRepository;
        this.usuarioService = usuarioService;
    }

    public ResponseDTO<Unidade> save(UnidadeFormDTO unidade){
        Unidade unidadeSalva = new Unidade();
        Usuario usuario = new Usuario();
        unidadeSalva = unidadeSalva.toUnidade(unidade);
        if(unidade.idUnidadeGerenciadora() > 0){
            unidadeSalva.setUnidadeGerenciadora(unidadeRepository.getById(unidade.idUnidadeGerenciadora()));
        }
        if (unidadeSalva.getEndereco() != null) {
            enderecoRepository.save(unidadeSalva.getEndereco());
        }
        unidadeSalva = unidadeRepository.save(unidadeSalva);
        usuario = usuarioService.save(unidade.usuarioRepresentante());
        unidadeRepository.salvarRepresentante(Math.toIntExact(unidadeSalva.getId()), Math.toIntExact(usuario.getId()));

        return ResponseDTO.ok( "Unidade cadastrada com sucesso!", unidadeSalva);
    }

    public List<Unidade> findAll(){
        return unidadeRepository.findAll().parallelStream().filter(Objects::nonNull).toList();
    }

    public List<Unidade> getGerenciadoras(String tipo){
        return unidadeRepository.getUnidadeByTipo(tipo);
    }

    public void delete(String uuid){
        Unidade unidade = new Unidade();
        unidade = this.unidadeRepository.findByUuid(uuid);
        System.out.println(unidade.getNome());
        unidadeRepository.delete(unidade);
    }

    public Unidade findByUuid(String uuid){
        return this.unidadeRepository.findByUuid(uuid);
    }

    public ResponseDTO<Unidade> update(UnidadeFormDTO unidade){
        Unidade unidadeSalva = new Unidade();
        Usuario usuario = new Usuario();
        unidadeSalva = unidadeSalva.toUnidade(unidade);
        if(unidade.idUnidadeGerenciadora()!= null && unidade.idUnidadeGerenciadora() > 0){
            unidadeSalva.setUnidadeGerenciadora(unidadeRepository.getById(unidade.idUnidadeGerenciadora()));
        }
        if (unidadeSalva.getEndereco() != null) {
            enderecoRepository.save(unidadeSalva.getEndereco());
        }
        unidadeSalva = unidadeRepository.save(unidadeSalva);
        if(unidade.usuarioRepresentante() != null) {
            usuario = usuarioService.save(unidade.usuarioRepresentante());
            unidadeRepository.salvarRepresentante(Math.toIntExact(unidadeSalva.getId()), Math.toIntExact(usuario.getId()));
        }
        return ResponseDTO.ok( "Unidade atualizada com sucesso!", unidadeSalva);
    }
}
