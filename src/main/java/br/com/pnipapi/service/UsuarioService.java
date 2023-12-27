package br.com.pnipapi.service;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UsuarioInfo;
import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.repository.UsuarioRepository;
import br.com.pnipapi.utils.User;
import org.apache.logging.log4j.util.Strings;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UsuarioService {

    UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
                    usuario.isAtivo());
        }).filter(Objects::nonNull).toList();
    }
    public List<UsuarioInfo> findRepresentantesUnidade(String uuid){
        return usuarioRepository.findRepresentantesUnidade(uuid).parallelStream().map(usuario -> {
            return new UsuarioInfo(
                usuario.getId(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getNome(),
                usuario.getDataCadastro(),
                usuario.getUltimoAcesso(),
                usuario.getUuid(),
                usuario.isAtivo());
        }).filter(Objects::nonNull).toList();
    }

    List<Usuario>findRepresentantes(long id_unidade){
        return this.usuarioRepository.findRepresentantes(id_unidade);
    }

}
