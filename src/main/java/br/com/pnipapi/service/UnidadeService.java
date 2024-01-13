package br.com.pnipapi.service;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.model.*;
import br.com.pnipapi.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UnidadeService {
    UnidadeRepository unidadeRepository;
    EnderecoRepository enderecoRepository;
    UsuarioService usuarioService;
    TipoUnidadeRepository tipoUnidadeRepository;
    UnidadeUsuarioRepository unidadeUsuarioRepository;
    PermissaoRepository permissaoRepository;
    public UnidadeService(UnidadeRepository unidadeRepository, EnderecoRepository enderecoRepository,
                          UsuarioService usuarioService, TipoUnidadeRepository tipoUnidadeRepository,
                          UnidadeUsuarioRepository unidadeUsuarioRepository,  PermissaoRepository permissaoRepository) {
        this.unidadeRepository = unidadeRepository;
        this.enderecoRepository = enderecoRepository;
        this.usuarioService = usuarioService;
        this.tipoUnidadeRepository = tipoUnidadeRepository;
        this.unidadeUsuarioRepository = unidadeUsuarioRepository;
        this.permissaoRepository = permissaoRepository;
    }

    @Transactional
    public ResponseDTO<Unidade> save(UnidadeFormDTO unidade) {
        try {
            Unidade unidadeSalva = new Unidade();
            unidadeSalva = unidadeSalva.toUnidade(unidade);

            if (unidade.idUnidadeGerenciadora() > 0) {
                unidadeSalva.setUnidadeGerenciadora(unidadeRepository.getById(unidade.idUnidadeGerenciadora()));
            }

            List<UnidadeUsuario> unidadeUsuarios = new ArrayList<>();

            Unidade finalUnidadeSalva = unidadeSalva;
            unidadeSalva.getUsuarios().forEach(usuario -> {
                List<Permissao> permissoes = permissaoRepository.findAllByDescricaoIn(
                    usuario.getPermissoes().stream().map(Permissao::getDescricao).toList());

                usuario.setPermissoes(new ArrayList<>());

                permissoes.forEach(permissao -> {
                    System.out.println(permissao.toString());
                    usuario.getPermissoes().add(permissao);

                    UnidadeUsuario uu = new UnidadeUsuario(finalUnidadeSalva, usuario, permissao, true);
                    unidadeUsuarios.add(uu);
                });
            });
            unidadeUsuarioRepository.saveAll(unidadeUsuarios);

            return ResponseDTO.ok("Unidade cadastrada com sucesso!", unidadeSalva);
        } catch (DataIntegrityViolationException e) {
            // Captura específica para violações de integridade
            return ResponseDTO.err("Erro ao cadastrar unidade: Violação de integridade de dados.");
        } catch (Exception e) {
            // Captura de exceções gerais
            System.out.println(e.getCause() + e.getMessage());
            return ResponseDTO.err("Erro ao cadastrar unidade" + e.getCause() + e.getMessage());
        }
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
