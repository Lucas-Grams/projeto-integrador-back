package br.com.pnipapi.service;
import br.com.pnipapi.dto.EmpresaUsuarioDTO;
import br.com.pnipapi.model.Empresa;
import br.com.pnipapi.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EmpresaService {

    private EmpresaRepository empresaRepository;
    private EmpresaUsuarioService empresaUsuarioService;
    private UsuarioService usuarioService;

    public EmpresaService(EmpresaRepository empresaRepository, EmpresaUsuarioService empresaUsuarioService,
                          UsuarioService usuarioService){
        this.empresaRepository = empresaRepository;
        this.empresaUsuarioService = empresaUsuarioService;
        this.usuarioService = usuarioService;
    }

    public List<Empresa> findAll(){
        return empresaRepository.findAll();
    }

    public Empresa findEmpresaByUuid(String uuid){
        Empresa empresa = new Empresa();
        try{
            UUID uuidObj = UUID.fromString(uuid);
            empresa = empresaRepository.findEmpresaByUuid(uuidObj);
            if(empresa != null){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return empresa;
    }

    public String ativaInativa(String uuid){
        try{
            Empresa empresa = new Empresa();
            UUID uuidObj = UUID.fromString(uuid);
            empresa = empresaRepository.findEmpresaByUuid(uuidObj);
            if (!empresa.isAtivo()) {
                empresa.setAtivo(true);
            } else {
               empresa.setAtivo(false);
            }
            empresa = empresaRepository.save(empresa);
            if(empresa != null){
                return "OK";

            }else{
                return "ERROR";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "ERROR";
        }
    }

    public String salvar(List<EmpresaUsuarioDTO> empUser){
        try{
            AtomicBoolean temUsuario = new AtomicBoolean(true);
            empUser.forEach((emp)->{
                if(!(emp.getUsuario().getNome().length()>2)){
                    temUsuario.set(false);
                }
            });
            if(!temUsuario.get()){
                Empresa empresa = new Empresa();
                EmpresaUsuarioDTO empresaUsuario = empUser.get(0);
                empresa = empresaUsuario.getEmpresa();
                empresa = empresaRepository.save(empresa);
                return "OK";
            }else{
                return this.empresaUsuarioService.saveUnidadeUsuario(empUser);
            }
        }catch (Exception e){
            e.printStackTrace();
            return "ERROR";
        }
    }
}
