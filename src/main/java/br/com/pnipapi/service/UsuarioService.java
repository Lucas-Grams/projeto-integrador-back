package br.com.pnipapi.service;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeUsuarioDTO;
import br.com.pnipapi.dto.UsuarioInfo;
import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.repository.UnidadeRepository;
import br.com.pnipapi.repository.UnidadeUsuarioRepository;
import br.com.pnipapi.repository.UsuarioRepository;
import br.com.pnipapi.utils.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    UsuarioRepository usuarioRepository;
    UnidadeUsuarioRepository unidadeUsuarioRepository;
    UnidadeRepository unidadeRepository;
    UnidadeUsuarioService unidadeUsuarioService;

    public UsuarioService(UsuarioRepository usuarioRepository, UnidadeUsuarioRepository unidadeUsuarioRepository,
                          UnidadeRepository unidadeRepository, UnidadeUsuarioService unidadeUsuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.unidadeUsuarioRepository = unidadeUsuarioRepository;
        this.unidadeRepository = unidadeRepository;
        this.unidadeUsuarioService = unidadeUsuarioService;
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

    public Usuario findUsuarioByUuid(String uuid){
        UUID uuidObj = UUID.fromString(uuid);
        return usuarioRepository.findAllByUuid(uuidObj).get();
    }

    public List<Usuario> findUsuariosUnidade(String uuid) {
        List<Usuario> usuarios = usuarioRepository.findUsuariosByUuidUnidade(uuid);

        Map<Long, Usuario> usuarioMap = usuarios.stream()
            .collect(Collectors.toMap(Usuario::getId, Function.identity(), (existing, replacement) -> replacement));
        List<Usuario> usuariosUnicos = usuarioMap.values().stream().collect(Collectors.toList());

        return usuariosUnicos;
    }

    public Optional<Usuario> findById(Long id){return this.usuarioRepository.findById(id);}

    public List<Usuario> findUsuariosDip(){
        return usuarioRepository.findUsuariosDip();
    }


    List<Usuario>findRepresentantes(long id_unidade){
        return this.usuarioRepository.findRepresentantes(id_unidade);
    }

    public String saveUsuarioUnidade(List<UnidadeUsuarioDTO> unidadeUsuarios){
        try{
            AtomicBoolean temUnidade = new AtomicBoolean(true);
            unidadeUsuarios.forEach((uni)->{
                if(!(uni.getUnidade().getNome().length()>2)){
                    temUnidade.set(false);
                }
            });
            if(!temUnidade.get()){
                Usuario user = new Usuario();
                UnidadeUsuarioDTO uniUser = new UnidadeUsuarioDTO();
                uniUser = unidadeUsuarios.get(0);
                user = uniUser.getUsuario();
                user = this.save(user);
                return "OK";
            }else{
                return this.unidadeUsuarioService.saveUnidadeUsuario(unidadeUsuarios);
            }
        }catch (Exception e){
            e.printStackTrace();
            return "ERROR";
        }
    }

    public String ativaInativa(String uuid){
        try{
            Usuario user = new Usuario();
            UUID uuidObj = UUID.fromString(uuid);
            user = usuarioRepository.findAllByUuid(uuidObj).get();
            if (!user.isAtivo()) {
                user.setAtivo(true);
            } else {
                user.setAtivo(false);
            }
            user = usuarioRepository.save(user);
            if(user != null){
                return "OK";

            }else{
                return "ERROR";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "ERROR";
        }
    }

    public List<Permissao> findPermissoesByUsuarioId(Long id, Long id_unidade){
        return this.unidadeUsuarioRepository.findPermissoesByUsuarioId(id, id_unidade);
    }

}


