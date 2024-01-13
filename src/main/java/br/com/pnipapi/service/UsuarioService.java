package br.com.pnipapi.service;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UsuarioInfo;
import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.repository.UsuarioRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

@Service
public class UsuarioService {

    UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public ResponseDTO<UsuarioInfo> salvar(Usuario usuario) {
        if (usuario.getSenha() != null && Strings.isNotBlank(usuario.getSenha()) && Strings.isNotEmpty(usuario.getSenha())) {
            final String senha = new BCryptPasswordEncoder().encode(usuario.getSenha());
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
            final String senha = new BCryptPasswordEncoder().encode(usuario.getSenha());
            usuario.setSenha(senha);
        }else{
            final String senha = new BCryptPasswordEncoder().encode("teste001");
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

    public List<Usuario> findUsuariosUnidade(String uuid) {
        List<Usuario> usuarios = usuarioRepository.findUsuariosUnidade(uuid);

        Map<Long, Usuario> usuarioMap = usuarios.stream()
            .collect(Collectors.toMap(Usuario::getId, Function.identity(), (existing, replacement) -> replacement));

        List<Usuario> usuariosUnicos = usuarioMap.values().stream().collect(Collectors.toList());
        return usuariosUnicos;
    }


    List<Usuario>findRepresentantes(long id_unidade){
        return this.usuarioRepository.findRepresentantes(id_unidade);
    }

}
