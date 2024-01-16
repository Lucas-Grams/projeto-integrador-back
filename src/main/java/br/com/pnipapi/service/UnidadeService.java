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
            Unidade finalUnidadeSalva1 = unidadeSalva;
            unidadeSalva.getUsuarios().forEach(usuario -> {
                if(usuario.getId() != null){
                    usuario = usuarioService.findById(usuario.getId()).get();
                    Usuario finalUsuario1 = usuario;
                    finalUnidadeSalva1.getUsuarios().forEach((user)->{
                        if(finalUsuario1.getId() == user.getId()){
                            finalUsuario1.setPermissoes(user.getPermissoes());
                        }
                    });
                }
                List<Permissao> permissoes = permissaoRepository.findAllByDescricaoIn(
                    usuario.getPermissoes().stream().map(Permissao::getDescricao).toList());

                usuario.setPermissoes(new ArrayList<>());

                Usuario finalUsuario = usuario;
                permissoes.forEach(permissao -> {
                    finalUsuario.getPermissoes().add(permissao);

                    UnidadeUsuario uu = new UnidadeUsuario(finalUnidadeSalva, finalUsuario, permissao, true);
                    unidadeUsuarios.add(uu);
                });
            });
            unidadeUsuarios.forEach((uniUser) ->{
                unidadeUsuarioRepository.saveAndFlush(uniUser);
            });

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
        try {
            Unidade unidadeSalva = new Unidade();
            unidadeSalva = unidadeSalva.toUnidade(unidade);

            if (unidade.idUnidadeGerenciadora() > 0) {
                unidadeSalva.setUnidadeGerenciadora(unidadeRepository.getById(unidade.idUnidadeGerenciadora()));
            }

           this.unidadeRepository.save(unidadeSalva);
            return ResponseDTO.ok("Unidade atualizada com sucesso!", unidadeSalva);
        } catch (DataIntegrityViolationException e) {
            // Captura específica para violações de integridade
            return ResponseDTO.err("Erro ao atualizar unidade: Violação de integridade de dados.");
        } catch (Exception e) {
            // Captura de exceções gerais
            System.out.println(e.getCause() + e.getMessage());
            return ResponseDTO.err("Erro ao atualizada unidade" + e.getCause() + e.getMessage());
        }
    }

    public void validaVinculo(List<UnidadeUsuario> unidadeUsuarios, Long unidadeId){
        List<UnidadeUsuario> vinculosExistentes = new ArrayList<>();
        vinculosExistentes = unidadeUsuarioRepository.findAllByUnidadeId(unidadeId);
        vinculosExistentes.forEach((vinculos)->{

        });
    }

    public ResponseDTO<List<TipoUnidade>> findAllTipos(){
        return ResponseDTO.ok(this.tipoUnidadeRepository.findAll());
    }

}
