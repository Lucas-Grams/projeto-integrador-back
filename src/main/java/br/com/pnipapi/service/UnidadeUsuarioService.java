package br.com.pnipapi.service;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeUsuarioDTO;
import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.model.UnidadeUsuario;
import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.repository.PermissaoRepository;
import br.com.pnipapi.repository.UnidadeRepository;
import br.com.pnipapi.repository.UnidadeUsuarioRepository;
import br.com.pnipapi.repository.UsuarioRepository;
import br.com.pnipapi.utils.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static br.com.pnipapi.dto.ResponseDTO.ok;

@Service
public class UnidadeUsuarioService{
    UnidadeUsuarioRepository unidadeUsuarioRepository;
    UsuarioRepository usuarioRepository;
    PermissaoRepository permissaoRepository;
    UnidadeRepository unidadeRepository;


    public UnidadeUsuarioService(UnidadeUsuarioRepository unidadeUsuarioRepository, UsuarioRepository usuarioRepository, PermissaoRepository permissaoRepository,
                                 UnidadeRepository unidadeRepository){
        this.unidadeUsuarioRepository = unidadeUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.permissaoRepository = permissaoRepository;
        this.unidadeRepository = unidadeRepository;
    }


        public List<UnidadeUsuarioDTO> findUnidadesByUsuarioUuid(String uuid) {
            UUID uuidObj = UUID.fromString(uuid);
            Usuario user = usuarioRepository.findByUuid(uuidObj).orElse(null);

            if (user == null) {
                // Trate o caso em que o usuário não é encontrado
                return new ArrayList<>();
            }

            List<UnidadeUsuario> vinculos = unidadeUsuarioRepository.findAllByUsuario_Id(user.getId());
            List<UnidadeUsuarioDTO> vinculosRetorno = new ArrayList<>();

            vinculos.forEach((vin) -> {
                if (!vinculosRetorno.contains(vin)) {
                    // Crie uma nova instância e adicione à lista de retorno
                    UnidadeUsuarioDTO novaInstancia = new UnidadeUsuarioDTO();
                    novaInstancia.setId(vin.getId());
                    novaInstancia.setUnidade(vin.getUnidade());
                    novaInstancia.setUsuario(vin.getUsuario());
                    novaInstancia.setPermissao(List.of(new Permissao[]{vin.getPermissao()}));
                    novaInstancia.setAtivo(vin.isAtivo());
                    vinculosRetorno.add(novaInstancia);
                } else {
                    // Encontre a instância existente e adicione a permissão
                    UnidadeUsuarioDTO instanciaExistente = vinculosRetorno.stream()
                        .filter(existing -> existing.equals(vin))
                        .findFirst()
                        .orElse(null);

                    if (instanciaExistente != null) {
                        Arrays.stream(new List[]{instanciaExistente.getPermissao()}).toList().add((List) vin.getPermissao());
                    }
                }
            });

            return vinculosRetorno;
        }

    public ResponseDTO saveUnidadeUsuario(List<UnidadeUsuarioDTO> unidadeUsuarios){

        List<UnidadeUsuario>  unidadeUsuarioSalvar = new ArrayList<>();

        List<UnidadeUsuario> finalUnidadeUsuarioSalvar = unidadeUsuarioSalvar;
        unidadeUsuarios.forEach((uni)->{
            Unidade unidadeSalvar = unidadeRepository.getById(uni.getUnidade().getId());
            if(uni.getUsuario().getId() != null){
                uni.setUsuario(usuarioRepository.getById(uni.getUsuario().getId()));
            }
            for (Permissao permissao : uni.getPermissao()) {
                UnidadeUsuario uniUsu = new UnidadeUsuario();
                uniUsu.setUnidade(unidadeSalvar);

                if (uni.getUsuario().getSenha() != null && Strings.isNotBlank(uni.getUsuario().getSenha()) && Strings.isNotEmpty(uni.getUsuario().getSenha())) {
                    final String senha = User.generatePasswordBCrypt(uni.getUsuario().getSenha());
                    uni.getUsuario().setSenha(senha);
                }else{
                    final String senha = User.generatePasswordBCrypt("teste001");
                    uni.getUsuario().setSenha(senha);
                }

                uniUsu.setUsuario(uni.getUsuario());
                Permissao permissao1 = permissaoRepository.findPermissaoByName(permissao.getDescricao());
                uniUsu.setPermissao(permissao1);
                finalUnidadeUsuarioSalvar.add(uniUsu);
            }
        });
        unidadeUsuarioSalvar = unidadeUsuarioRepository.saveAllAndFlush(unidadeUsuarioSalvar);
        if (unidadeUsuarios.isEmpty()) {
            return ResponseDTO.err("Erro ao cadastrar usuario");
        } else {
            return ResponseDTO.ok("Usuario cadastrado com sucesso");
        }

    }


    }
