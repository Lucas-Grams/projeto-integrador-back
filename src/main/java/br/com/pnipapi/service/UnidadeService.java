package br.com.pnipapi.service;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeUsuarioDTO;
import br.com.pnipapi.model.*;
import br.com.pnipapi.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UnidadeService {
    UnidadeRepository unidadeRepository;
    EnderecoRepository enderecoRepository;
    UsuarioService usuarioService;
    TipoUnidadeRepository tipoUnidadeRepository;
    UnidadeUsuarioRepository unidadeUsuarioRepository;
    PermissaoRepository permissaoRepository;
    UnidadeUsuarioService unidadeUsuarioService;

    public UnidadeService(UnidadeRepository unidadeRepository, EnderecoRepository enderecoRepository,
        UsuarioService usuarioService, TipoUnidadeRepository tipoUnidadeRepository,
        UnidadeUsuarioRepository unidadeUsuarioRepository, PermissaoRepository permissaoRepository,
        UnidadeUsuarioService unidadeUsuarioService) {
        this.unidadeRepository = unidadeRepository;
        this.enderecoRepository = enderecoRepository;
        this.usuarioService = usuarioService;
        this.tipoUnidadeRepository = tipoUnidadeRepository;
        this.unidadeUsuarioRepository = unidadeUsuarioRepository;
        this.permissaoRepository = permissaoRepository;
        this.unidadeUsuarioService = unidadeUsuarioService;
    }

    @Transactional
    public String saveUnidadeUsuario(List<UnidadeUsuarioDTO> unidadeUsuarios) {
        try {
            Unidade unidadeSalva;
            AtomicBoolean temUsuario = new AtomicBoolean(true);
            unidadeUsuarios.forEach((uni) -> {
                if (uni.getUsuario().getCpf() == null) {
                    temUsuario.set(false);
                }
            });

            if (!temUsuario.get()) {
                unidadeSalva = new Unidade();
                UnidadeUsuarioDTO uniUser = new UnidadeUsuarioDTO();
                uniUser = unidadeUsuarios.get(0);
                unidadeSalva = uniUser.getUnidade().toUnidade(uniUser.getUnidade());
                if (unidadeSalva.getUnidadeGerenciadora().getId() > 0) {
                    unidadeSalva.setUnidadeGerenciadora(unidadeRepository.getById(unidadeSalva.getUnidadeGerenciadora().getId()));
                }
                unidadeSalva = unidadeRepository.save(unidadeSalva);
                return "OK";
            } else {
                return this.unidadeUsuarioService.saveUsuarioUnidade(unidadeUsuarios);
            }
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return "ERROR";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public List<Unidade> findAll() {
        return unidadeRepository.findAll().parallelStream().filter(Objects::nonNull).toList();
    }

    public List<Unidade> getGerenciadoras(String tipo){
        return unidadeRepository.findUnidadesByTipo(tipo);
    }

    @Transactional
    public void inativa(String uuid){
        Unidade unidade = this.unidadeRepository.findUnidadeByUuid(uuid);
        unidade.setUsuarios(this.usuarioService.findUsuariosUnidade(uuid));
        if(!unidade.getUsuarios().isEmpty()) {
            unidadeRepository.updateUsuariosByIdUnidade(unidade.getId(), unidade.isAtivo() ? false : true);
        }
        unidade.setAtivo(unidade.isAtivo() ? false : true);
        unidadeRepository.save(unidade);
    }

    public Unidade findByUuid(String uuid){
        return this.unidadeRepository.findUnidadeByUuid(uuid);
    }

    public void validaVinculo(List<UnidadeUsuario> unidadeUsuarios, Long unidadeId) {
        List<UnidadeUsuario> vinculosExistentes = new ArrayList<>();
        vinculosExistentes = unidadeUsuarioRepository.findAllByUnidadeId(unidadeId);
        vinculosExistentes.forEach((vinculos) -> {

        });
    }

    public ResponseDTO<List<TipoUnidade>> findAllTipos() {
        return ResponseDTO.ok(this.tipoUnidadeRepository.findAll());
    }

}
