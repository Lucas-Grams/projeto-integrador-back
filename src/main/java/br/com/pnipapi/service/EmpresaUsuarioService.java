package br.com.pnipapi.service;
import br.com.pnipapi.dto.EmpresaUsuarioDTO;
import br.com.pnipapi.model.*;
import br.com.pnipapi.repository.EmpresaRepository;
import br.com.pnipapi.repository.EmpresaUsuarioRepository;
import br.com.pnipapi.repository.PermissaoRepository;
import br.com.pnipapi.repository.UsuarioRepository;
import br.com.pnipapi.utils.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmpresaUsuarioService {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;
    private EmpresaRepository empresaRepository;
    private PermissaoRepository permissaoRepository;
    private EmpresaUsuarioRepository empresaUsuarioRepository;
    public EmpresaUsuarioService(UsuarioRepository usuarioRepository, UsuarioService usuarioService,
                                 EmpresaRepository empresaRepository,PermissaoRepository permissaoRepository,
                                 EmpresaUsuarioRepository empresaUsuarioRepository){
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.empresaRepository = empresaRepository;
        this.permissaoRepository = permissaoRepository;
        this.empresaUsuarioRepository = empresaUsuarioRepository;
    }

    @Transactional
    public String saveUnidadeUsuario(List<EmpresaUsuarioDTO> empUser) {
        Empresa empresa = new Empresa();
        if (empUser.isEmpty()) {
            return "ERROR";
        }

        List<EmpresaUsuario> empresaUsuarioSalvar = new ArrayList<>();
        Empresa empresaSalvar = new Empresa();
        for (EmpresaUsuarioDTO emp : empUser) {
            Usuario usuario = new Usuario();
            if(emp.getId() == null) {
                if (emp.getEmpresa().getId() == null) {
                    if(empresaRepository.countEmpresasByCnpjAndAtivo(emp.getEmpresa().getCnpj(), true)<=0) {
                        empresaSalvar = empresaRepository.save(emp.getEmpresa());
                    }else{
                        empresaSalvar = empresaRepository.findByCnpj(emp.getEmpresa().getCnpj()).get();
                    }
                }else{
                    empresaSalvar =  empresaRepository.save(emp.getEmpresa());
                }
                if(emp.getUsuario().getId() == null){
                    usuario = this.saveOrUpdateUsuario(emp.getUsuario());
                }else{
                    usuario = usuarioRepository.getById(emp.getUsuario().getId());
                }
                empresaUsuarioSalvar.addAll(createEmpresaUsuarioList(empresaSalvar, usuario, (List<Permissao>) emp.getPermissao(), true));
            }else{
                empresaSalvar = empresaRepository.save(emp.getEmpresa());
               if(emp.getUsuario().getId() == null){
                   usuario = saveOrUpdateUsuario(emp.getUsuario());
               }else{
                   usuario = usuarioRepository.getById(emp.getUsuario().getId());
               }
                empresaUsuarioSalvar.addAll(createEmpresaUsuarioListAtualiza(empresaSalvar, usuario, (List<Permissao>) emp.getPermissao(), emp.isAtivo()));
            }
        }

        empresaUsuarioRepository.saveAllAndFlush(empresaUsuarioSalvar);
        this.validaPermissoes(empresaUsuarioSalvar);
        return "OK";
    }

    private Usuario saveOrUpdateUsuario(Usuario usuario) {
        String senha = Strings.isNotBlank(usuario.getSenha()) ? User.generatePasswordBCrypt(usuario.getSenha()) :
            User.generatePasswordBCrypt("teste001");
        usuario.setSenha(senha);
        return usuarioRepository.save(usuario);
    }

    private List<EmpresaUsuario> createEmpresaUsuarioList(Empresa empresa, Usuario usuario, List<Permissao> permissoes, boolean ativo) {
        return permissoes.stream()
            .map(permissao -> {
                EmpresaUsuario empUser = new EmpresaUsuario();
                empUser.setEmpresa(empresa);
                empUser.setUsuario(usuario);
                empUser.setPermissao(permissaoRepository.findPermissaoByName(permissao.getDescricao()));
                empUser.setAtivo(ativo);
                return empUser;
            })
            .collect(Collectors.toList());
    }

    private List<EmpresaUsuario> createEmpresaUsuarioListAtualiza(Empresa empresa, Usuario usuario, List<Permissao> permissoes, boolean ativo) {
        return permissoes.stream()
            .map(permissao -> {
                EmpresaUsuario empUser = new EmpresaUsuario();
                permissao = permissaoRepository.findPermissaoByName(permissao.getDescricao());
                empUser.setId(empresaUsuarioRepository.findIdByIdUsuarioIdPermissao(usuario.getId(), permissao.getId(), empresa.getId()));
                empUser.setEmpresa(empresa);
                empUser.setUsuario(usuario);
                empUser.setPermissao(permissao);
                empUser.setAtivo(ativo);
                return empUser;
            })
            .collect(Collectors.toList());
    }

    void validaPermissoes(List<EmpresaUsuario> empUser) {
        if (empUser.isEmpty()) {
            return;
        }

        Usuario user = empUser.stream().findFirst().map(EmpresaUsuario::getUsuario).orElse(null);
        if (user == null) {
            return;
        }

        List<Permissao> permissoesUnicas = empUser.stream()
            .map(EmpresaUsuario::getPermissao)
            .collect(Collectors.toList());
        permissoesUnicas.forEach((perm)->{
            if (usuarioRepository.countPermissaoByUsuarioIdPermissaoId(user.getId(), perm.getId()) == 0) {
                usuarioRepository.savePermissao(user.getId(), perm.getId());
            }
        });
    }

    public List<EmpresaUsuarioDTO> findUsuariosByUuidEmpresa(String uuid){
        UUID uuidObj = UUID.fromString(uuid);
        Empresa empresa = empresaRepository.findEmpresaByUuid(uuidObj);
        if (empresa == null) {
            return new ArrayList<>();
        }
        List<EmpresaUsuario> vinculos = empresaUsuarioRepository.findAllByEmpresaId(empresa.getId());
        List<EmpresaUsuarioDTO> vinculosRetorno = new ArrayList<>();

        vinculos.forEach((vin) -> {
            if (!vinculosRetorno.contains(vin)) {
                // Crie uma nova instância e adicione à lista de retorno
                EmpresaUsuarioDTO novaInstancia = new EmpresaUsuarioDTO();
                novaInstancia.setId(vin.getId());
                novaInstancia.setEmpresa(vin.getEmpresa());
                novaInstancia.setUsuario(vin.getUsuario());
                novaInstancia.setPermissao(List.of(new Permissao[]{vin.getPermissao()}));
                novaInstancia.setAtivo(vin.isAtivo());
                vinculosRetorno.add(novaInstancia);
            } else {
                // Encontre a instância existente e adicione a permissão
                EmpresaUsuarioDTO instanciaExistente = vinculosRetorno.stream()
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
}
