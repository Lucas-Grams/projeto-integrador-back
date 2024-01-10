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

import java.sql.Date;
import java.time.LocalDate;
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
        List<Usuario> usuarios = new ArrayList<>();
        unidadeSalva = unidadeSalva.toUnidade(unidade);
        if(unidade.idUnidadeGerenciadora() > 0){
            unidadeSalva.setUnidadeGerenciadora(unidadeRepository.getById(unidade.idUnidadeGerenciadora()));
        }
        if (unidadeSalva.getEndereco() != null) {
            enderecoRepository.save(unidadeSalva.getEndereco());
        }
        unidade.usuarios().forEach((user)->{
            user.setDataCadastro(Date.valueOf(LocalDate.now()));
            usuarios.add(usuarioService.save(user));
        });

        if (Objects.nonNull(unidadeSalva.getUsuarios())) {
            unidadeSalva.setUsuarios(usuarios);
        } else {
            unidadeSalva.setUsuarios(new ArrayList<>());
            unidadeSalva.setUsuarios(usuarios);
        }
        System.out.println(unidadeSalva.getUsuarios().size());
        unidadeSalva = unidadeRepository.save(unidadeSalva);
        Unidade finalUnidadeSalva = unidadeSalva;
        unidadeSalva.getUsuarios().forEach((user)->{unidadeRepository.salvarRepresentante(Math.toIntExact(finalUnidadeSalva.getId()), Math.toIntExact(user.getId()));});
        return ResponseDTO.ok( "Unidade cadastrada com sucesso!", unidadeSalva);
    }

    public List<Unidade> findAll(){
        return unidadeRepository.findAll().parallelStream().filter(Objects::nonNull).toList();
    }

    public List<Unidade> getGerenciadoras(String tipo){
        return unidadeRepository.getUnidadeByTipo(tipo);
    }

    @Transactional
    public void inativa(String uuid){
        Unidade unidade = this.unidadeRepository.findByUuid(uuid);
        unidade.setUsuarios(this.usuarioService.findRepresentantes(unidade.getId()));
        if(!unidade.getUsuarios().isEmpty()) {
            unidadeRepository.updateRepresentante(unidade.getId(), unidade.isAtivo() ? false : true);
        }
        unidade.setAtivo(unidade.isAtivo() ? false : true);
        unidadeRepository.save(unidade);
    }

    public Unidade findByUuid(String uuid){
        return this.unidadeRepository.findByUuid(uuid);
    }

    public ResponseDTO<Unidade> update(UnidadeFormDTO unidade){
        System.out.println(unidade.toString());
        Unidade unidadeSalva = new Unidade();
        List<Usuario> usuarios = new ArrayList<>();
        unidadeSalva = unidadeSalva.toUnidade(unidade);
        if(unidade.idUnidadeGerenciadora()!= null && unidade.idUnidadeGerenciadora() > 0){
            unidadeSalva.setUnidadeGerenciadora(unidadeRepository.getById(unidade.idUnidadeGerenciadora()));
        }
        System.out.println(unidadeSalva.toString());
        if (unidadeSalva.getEndereco() != null) {
            enderecoRepository.save(unidadeSalva.getEndereco());
        }

        unidade.usuarios().forEach((user)->{
            usuarios.add(usuarioService.save(user));
        });

        if (Objects.nonNull(unidadeSalva.getUsuarios())) {
            unidadeSalva.setUsuarios(usuarios);
        } else {
            unidadeSalva.setUsuarios(new ArrayList<>());
            unidadeSalva.setUsuarios(usuarios);
        }

        unidadeSalva = unidadeRepository.save(unidadeSalva);
        Unidade finalUnidadeSalva = unidadeSalva;
        unidadeSalva.getUsuarios().forEach((user)->{unidadeRepository.salvarRepresentante(Math.toIntExact(finalUnidadeSalva.getId()), Math.toIntExact(user.getId()));});

        return ResponseDTO.ok( "Unidade atualizada com sucesso!", unidadeSalva);
    }

    public ResponseDTO<List<TipoUnidade>> findAllTipos(){
        return ResponseDTO.ok(this.tipoUnidadeRepository.findAll());
    }
}
