package br.com.pnipapi.service;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UsuarioInfo;
import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.repository.UnidadeRepository;
import br.com.pnipapi.repository.UnidadeUsuarioRepository;
import br.com.pnipapi.repository.UsuarioRepository;
import br.com.pnipapi.utils.User;
import org.apache.logging.log4j.util.Strings;

import org.hibernate.collection.internal.PersistentBag;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    UsuarioRepository usuarioRepository;
    UnidadeUsuarioRepository unidadeUsuarioRepository;
    UnidadeRepository unidadeRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, UnidadeUsuarioRepository unidadeUsuarioRepository,
                          UnidadeRepository unidadeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.unidadeUsuarioRepository = unidadeUsuarioRepository;
        this.unidadeRepository = unidadeRepository;
    }

    public ResponseDTO<UsuarioInfo> salvar(Usuario usuario) {
        if (usuario.getSenha() != null && Strings.isNotBlank(usuario.getSenha()) && Strings.isNotEmpty(usuario.getSenha())) {
            final String senha = User.generatePasswordBCrypt(usuario.getSenha());
            usuario.setSenha(senha);
        }
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return ResponseDTO.ok("Usuário salvo com sucesso", UsuarioInfo.builder()
                .cpf(usuarioSalvo.getCpf())
                .id(usuarioSalvo.getId())
                .nome(usuarioSalvo.getNome())
                .email(usuarioSalvo.getEmail())
                .ativo(usuarioSalvo.isAtivo())
                .dataCadastro(usuarioSalvo.getDataCadastro()).build());
    }

    public Usuario save(Usuario usuario){

        if (usuario.getSenha() != null && Strings.isNotBlank(usuario.getSenha()) && Strings.isNotEmpty(usuario.getSenha())) {
            final String senha = User.generatePasswordBCrypt(usuario.getSenha());
            usuario.setSenha(senha);
        }else{
            final String senha = User.generatePasswordBCrypt("teste001");
            usuario.setSenha(senha);
        }
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioSalvo;
    }


    public List<UsuarioInfo> findAll() {
        return usuarioRepository.findAll().parallelStream().map(usuario -> {
            return new UsuarioInfo(
                    usuario.getId(),
                    usuario.getCpf(),
                    usuario.getEmail(),
                    usuario.getNome(),
                    usuario.getDataCadastro(),
                    usuario.getUltimoAcesso(),
                    usuario.getUuid(),
                    usuario.isAtivo(),
                    usuario.getPermissoes());
        }).filter(Objects::nonNull).toList();
    }

    public Usuario findByUuid(String uuid){
        UUID uuidObj = UUID.fromString(uuid);
        return usuarioRepository.findByUuid(uuidObj).get();
    }

    public List<Usuario> findUsuariosUnidade(String uuid) {
        List<Usuario> usuarios = usuarioRepository.findUsuariosUnidade(uuid);

//        usuarios.forEach((user)->{
//            List<Permissao> permissoes = this.findPermissoesByUsuarioId(user.getId(), this.unidadeRepository.findIdByUuid(uuid));
//            System.out.println(permissoes.toString());
//            user.setPermissoes(permissoes);
//        });

        Map<Long, Usuario> usuarioMap = usuarios.stream()
            .collect(Collectors.toMap(Usuario::getId, Function.identity(), (existing, replacement) -> replacement));
        List<Usuario> usuariosUnicos = usuarioMap.values().stream().collect(Collectors.toList());

        return usuariosUnicos;
    }

    public List<Permissao> findPermissoesByUsuarioId(Long id, Long id_unidade){
        return this.unidadeUsuarioRepository.findPermissoesByUsuarioId(id, id_unidade);
    }


    List<Usuario>findRepresentantes(long id_unidade){
        return this.usuarioRepository.findRepresentantes(id_unidade);
    }

    public Optional<Usuario> findById(Long id){return this.usuarioRepository.findById(id);}

    public List<Usuario> findUsuariosDip(){
        return usuarioRepository.findUsuariosDip();
    }
}
