package br.com.pnipapi.service;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeUsuarioDTO;
import br.com.pnipapi.model.*;
import br.com.pnipapi.repository.*;
import br.com.pnipapi.utils.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnidadeUsuarioService {

    UnidadeUsuarioRepository unidadeUsuarioRepository;
    UsuarioRepository usuarioRepository;
    PermissaoRepository permissaoRepository;
    UnidadeRepository unidadeRepository;
    EnderecoRepository enderecoRepository;


    public UnidadeUsuarioService(UnidadeUsuarioRepository unidadeUsuarioRepository, UsuarioRepository usuarioRepository,
        PermissaoRepository permissaoRepository,
        UnidadeRepository unidadeRepository, EnderecoRepository enderecoRepository) {
        this.unidadeUsuarioRepository = unidadeUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.permissaoRepository = permissaoRepository;
        this.unidadeRepository = unidadeRepository;
        this.enderecoRepository = enderecoRepository;
    }


    public List<UnidadeUsuarioDTO> findUnidadesByUsuarioUuid(String uuid) {
        UUID uuidObj = UUID.fromString(uuid);
        Usuario user = usuarioRepository.findAllByUuid(uuidObj).orElse(null);

        if (user == null) {
            return new ArrayList<>();
        }

        List<UnidadeUsuario> vinculos = unidadeUsuarioRepository.findAllByUsuario_Id(user.getId());
        List<UnidadeUsuarioDTO> vinculosRetorno = new ArrayList<>();

        vinculos.forEach((vin) -> {
            if (!vinculosRetorno.contains(vin)) {
                UnidadeUsuarioDTO novaInstancia = new UnidadeUsuarioDTO();
                novaInstancia.setId(vin.getId());
                novaInstancia.setUnidade(vin.getUnidade());
                novaInstancia.setUsuario(vin.getUsuario());
                novaInstancia.setPermissao(List.of(new Permissao[]{vin.getPermissao()}));
                novaInstancia.setAtivo(vin.isAtivo());
                vinculosRetorno.add(novaInstancia);
            } else {
                UnidadeUsuarioDTO instanciaExistente = vinculosRetorno.stream()
                    .filter(existing -> existing.equals(vin))
                    .findFirst()
                    .orElse(null);

                if (instanciaExistente != null) {
                    Arrays.stream(new List[]{instanciaExistente.getPermissao()}).toList()
                        .add((List) vin.getPermissao());
                }
            }
        });
        return vinculosRetorno;
    }

    public List<UnidadeUsuarioDTO> findUsuariosByUnidadeUuid(String uuid) {
        Unidade unidade = unidadeRepository.findUnidadeByUuid(uuid);
        if (unidade == null) {
            return new ArrayList<>();
        }
        List<UnidadeUsuario> vinculos = unidadeUsuarioRepository.findAllByUnidadeId(unidade.getId());
        List<UnidadeUsuarioDTO> vinculosRetorno = new ArrayList<>();

        vinculos.forEach((vin) -> {
            if (!vinculosRetorno.contains(vin)) {
                UnidadeUsuarioDTO novaInstancia = new UnidadeUsuarioDTO();
                novaInstancia.setId(vin.getId());
                novaInstancia.setUnidade(vin.getUnidade());
                novaInstancia.setUsuario(vin.getUsuario());
                novaInstancia.setPermissao(List.of(new Permissao[]{vin.getPermissao()}));
                novaInstancia.setAtivo(vin.isAtivo());
                vinculosRetorno.add(novaInstancia);
            } else {
                UnidadeUsuarioDTO instanciaExistente = vinculosRetorno.stream()
                    .filter(existing -> existing.equals(vin))
                    .findFirst()
                    .orElse(null);

                if (instanciaExistente != null) {
                    Arrays.stream(new List[]{instanciaExistente.getPermissao()}).toList()
                        .add((List) vin.getPermissao());
                }
            }
        });
        return vinculosRetorno;
    }

    @Transactional
    public String saveUnidadeUsuario(List<UnidadeUsuarioDTO> unidadeUsuarios) {
        Usuario usuario = new Usuario();
        if (unidadeUsuarios.isEmpty()) {
            return "ERROR";
        }
        List<UnidadeUsuario> unidadeUsuarioSalvar = new ArrayList<>();
        for (UnidadeUsuarioDTO uni : unidadeUsuarios) {
            if (uni.getId() == null) {
                Unidade unidadeSalvar = unidadeRepository.getById(uni.getUnidade().getId());

                if (usuario.getId() == null) {
                    usuario = saveOrUpdateUsuario(uni.getUsuario());
                }
                unidadeUsuarioSalvar.addAll(createUnidadeUsuarioList(unidadeSalvar, usuario, uni.getPermissao(), true));
            }else{
                Unidade unidadeSalvar = unidadeRepository.getById(uni.getUnidade().getId());
                usuario = saveOrUpdateUsuario(uni.getUsuario());
                unidadeUsuarioSalvar.addAll(
                    createUnidadeUsuarioListAtualiza(unidadeSalvar, usuario, uni.getPermissao(), uni.isAtivo()));
            }
        }

        unidadeUsuarioRepository.saveAllAndFlush(unidadeUsuarioSalvar);
        this.validaPermissoes(unidadeUsuarioSalvar);
        return "OK";
    }

    @Transactional
    public String saveUsuarioUnidade(List<UnidadeUsuarioDTO> unidadeUsuarios){
        try{
            Unidade unidade = new Unidade();
            if (unidadeUsuarios.isEmpty()) {
                return "ERROR";
            }

            List<UnidadeUsuario> unidadeUsuarioSalvar = new ArrayList<>();

            for (UnidadeUsuarioDTO uni : unidadeUsuarios) {
                unidade = uni.getUnidade();
                Usuario usuario = new Usuario();
                if(uni.getId() == null) {
                    if(uni.getUsuario().getId() == null){
                        if(usuarioRepository.countUsuarioByCpf(uni.getUsuario().getCpf()) == 0){
                            usuario = this.saveOrUpdateUsuario(uni.getUsuario());
                        }else{
                            usuario = usuarioRepository.findUsuarioByCpf(uni.getUsuario().getCpf()).get();
                        }
                    }else{
                        usuario = usuarioRepository.findUsuarioById(uni.getUsuario().getId()).get();
                    }


                    if (uni.getId() == null) {
                        unidade = unidadeRepository.save(unidade.toUnidade(unidade));
                    }
                    if(uni.getUnidade().getEndereco() != null) {
                        Endereco endereco = enderecoRepository.save(uni.getUnidade().getEndereco());
                        unidade.setEndereco(endereco);
                    }
                    unidadeUsuarioSalvar.addAll(createUnidadeUsuarioList(unidade, usuario, uni.getPermissao(), true));
                }else{
                    usuario = usuarioRepository.getById(uni.getUsuario().getId());
                    unidade = unidadeRepository.save(uni.getUnidade());
                    Endereco endereco = enderecoRepository.save(uni.getUnidade().getEndereco());
                    unidade.setEndereco(endereco);
                    unidadeUsuarioSalvar.addAll(createUnidadeUsuarioListAtualiza(unidade, usuario, uni.getPermissao(),uni.isAtivo()));
                }
            }
            unidadeUsuarioRepository.saveAllAndFlush(unidadeUsuarioSalvar);
            this.validaPermissoes(unidadeUsuarioSalvar);
            return "OK";
        }catch (Exception e){
            e.printStackTrace();
            return "ERROR";
        }
    }

    void validaPermissoes(List<UnidadeUsuario> unidadeUsuarios) {
        if (unidadeUsuarios.isEmpty()) {
            return;
        }

        Usuario user = unidadeUsuarios.stream().findFirst().map(UnidadeUsuario::getUsuario).orElse(null);
        if (user == null) {
            return;
        }

        List<Permissao> permissoesUnicas = unidadeUsuarios.stream()
            .map(UnidadeUsuario::getPermissao)
            .collect(Collectors.toList());
        permissoesUnicas.forEach((perm) -> {
            if (usuarioRepository.countPermissaoByUsuarioIdPermissaoId(user.getId(), perm.getId()) == 0) {
                usuarioRepository.savePermissao(user.getId(), perm.getId());
            }
        });
    }

    private Usuario saveOrUpdateUsuario(Usuario usuario) {
        String senha = Strings.isNotBlank(usuario.getSenha()) ? User.generatePasswordBCrypt(usuario.getSenha()) :
            User.generatePasswordBCrypt("teste001");
        usuario.setSenha(senha);
        return usuarioRepository.save(usuario);
    }

    private List<UnidadeUsuario> createUnidadeUsuarioList(Unidade unidade, Usuario usuario, List<Permissao> permissoes,
        boolean ativo) {
        return permissoes.stream()
            .map(permissao -> {
                UnidadeUsuario uniUsu = new UnidadeUsuario();
                uniUsu.setUnidade(unidade);
                uniUsu.setUsuario(usuario);
                uniUsu.setPermissao(permissaoRepository.findPermissaoByName(permissao.getDescricao()));
                uniUsu.setAtivo(ativo);
                return uniUsu;
            })
            .collect(Collectors.toList());
    }

    private List<UnidadeUsuario> createUnidadeUsuarioListAtualiza(Unidade unidade, Usuario usuario,
        List<Permissao> permissoes, boolean ativo) {
        return permissoes.stream()
            .map(permissao -> {
                UnidadeUsuario uniUsu = new UnidadeUsuario();
                permissao = permissaoRepository.findPermissaoByName(permissao.getDescricao());
                uniUsu.setId(unidadeUsuarioRepository.findIdByUsuarioIdPermissaoID(usuario.getId(), permissao.getId(),
                    unidade.getId()));
                uniUsu.setUnidade(unidade);
                uniUsu.setUsuario(usuario);
                uniUsu.setPermissao(permissao);
                uniUsu.setAtivo(ativo);
                return uniUsu;
            })
            .collect(Collectors.toList());
    }
}
