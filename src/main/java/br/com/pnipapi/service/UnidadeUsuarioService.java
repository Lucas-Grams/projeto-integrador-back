package br.com.pnipapi.service;

import br.com.pnipapi.dto.UnidadeUsuarioDTO;
import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.UnidadeUsuario;
import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.repository.UnidadeUsuarioRepository;
import br.com.pnipapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class UnidadeUsuarioService{
    UnidadeUsuarioRepository unidadeUsuarioRepository;
    UsuarioRepository usuarioRepository;
    public UnidadeUsuarioService(UnidadeUsuarioRepository unidadeUsuarioRepository, UsuarioRepository usuarioRepository){
        this.unidadeUsuarioRepository = unidadeUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
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
                    novaInstancia.setPermissao(new Permissao[]{vin.getPermissao()});
                    novaInstancia.setAtivo(vin.isAtivo());
                    vinculosRetorno.add(novaInstancia);
                } else {
                    // Encontre a instância existente e adicione a permissão
                    UnidadeUsuarioDTO instanciaExistente = vinculosRetorno.stream()
                        .filter(existing -> existing.equals(vin))
                        .findFirst()
                        .orElse(null);

                    if (instanciaExistente != null) {
                        Arrays.stream(instanciaExistente.getPermissao()).toList().add(vin.getPermissao());
                    }
                }
            });

            return vinculosRetorno;
        }

    }
