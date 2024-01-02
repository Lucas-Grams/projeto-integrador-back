package br.com.pnipapi.service;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.dto.UsuarioInfo;
import br.com.pnipapi.model.TipoUnidade;
import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.repository.EnderecoRepository;
import br.com.pnipapi.repository.TipoUnidadeRepository;
import br.com.pnipapi.repository.UnidadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UnidadeService {
    UnidadeRepository unidadeRepository;
    EnderecoRepository enderecoRepository;
    UsuarioService usuarioService;
    TipoUnidadeRepository tipoUnidadeRepository;
    public UnidadeService(UnidadeRepository unidadeRepository, EnderecoRepository enderecoRepository, UsuarioService usuarioService, TipoUnidadeRepository tipoUnidadeRepository) {
        this.unidadeRepository = unidadeRepository;
        this.enderecoRepository = enderecoRepository;
        this.usuarioService = usuarioService;
        this.tipoUnidadeRepository = tipoUnidadeRepository;
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
        usuario = usuarioService.save(unidade.usuarioRepresentante());

        if (Objects.nonNull(unidadeSalva.getUsuarios())) {
            unidadeSalva.getUsuarios().add(usuario);
        } else {
            unidadeSalva.setUsuarios(new ArrayList<>());
            unidadeSalva.getUsuarios().add(usuario);
        }
        System.out.println(unidadeSalva.getUsuarios().size());
        unidadeSalva = unidadeRepository.save(unidadeSalva);
        unidadeRepository.salvarRepresentante(Math.toIntExact(unidadeSalva.getId()), Math.toIntExact(usuario.getId()));

        return ResponseDTO.ok( "Unidade cadastrada com sucesso!", unidadeSalva);
    }

    public List<Unidade> findAll(){
        return unidadeRepository.findAllByAtivo(true).parallelStream().filter(Objects::nonNull).toList();
    }

    public List<Unidade> getGerenciadoras(String tipo){
        return unidadeRepository.getUnidadeByTipo(tipo);
    }

    @Transactional
    public void inativa(String uuid){
        Unidade unidade = this.unidadeRepository.findByUuid(uuid);
        unidade.setUsuarios(this.usuarioService.findRepresentantes(unidade.getId()));
        if(!unidade.getUsuarios().isEmpty()) {
            unidadeRepository.updateRepresentante(unidade.getId());
        }
        unidade.setAtivo(false);
        unidadeRepository.save(unidade);
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

        usuario = usuarioService.save(unidade.usuarioRepresentante());

        if (Objects.nonNull(unidadeSalva.getUsuarios())) {
            unidadeSalva.getUsuarios().add(usuario);
        } else {
            unidadeSalva.setUsuarios(new ArrayList<>());
            unidadeSalva.getUsuarios().add(usuario);
        }

        unidadeSalva = unidadeRepository.save(unidadeSalva);

        return ResponseDTO.ok( "Unidade atualizada com sucesso!", unidadeSalva);
    }

    public ResponseDTO<List<TipoUnidade>> findAllTipos(){
        return ResponseDTO.ok(this.tipoUnidadeRepository.findAll());
    }
}
