package br.com.pnipapi.controller;
import br.com.pnipapi.dto.EmpresaUsuarioDTO;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.model.Empresa;
import br.com.pnipapi.service.EmpresaService;
import br.com.pnipapi.service.EmpresaUsuarioService;
import br.com.pnipapi.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {

    private EmpresaService empresaService;
    private EmpresaUsuarioService empresaUsuarioService;
    private UsuarioService usuarioService;
    public EmpresaController(EmpresaService empresaService, EmpresaUsuarioService empresaUsuarioService,
                             UsuarioService usuarioService){
        this.empresaService = empresaService;
        this.empresaUsuarioService = empresaUsuarioService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/find-all")
        public List<Empresa> findAll(){
            return empresaService.findAll();
    }

    @GetMapping("/find-empresa-by-uuid/{uuid}")
    public Empresa findEmpresaByUuid(@PathVariable String uuid){
        return empresaService.findEmpresaByUuid(uuid);
    }

    @PostMapping("/ativa-inativa")
    public ResponseDTO ativaInativa(@RequestBody String uuid){
        String status = empresaService.ativaInativa(uuid);
        if ("OK".equals(status)) {
            return ResponseDTO.ok("Empresa Inativada/ativada com sucesso!");
        } else if ("ERROR".equals(status)) {
            return ResponseDTO.err("Houve um erro, a empresa não pode ser iantivada/ativada, tente mais tarde!");
        }
        return null;
    }

    @PostMapping("/salvar")
    public ResponseDTO salvar(@RequestBody List<EmpresaUsuarioDTO> empUser){
        String status = empresaService.salvar(empUser);
        if("OK".equals(status)){
            return ResponseDTO.ok("Empresa cadastrada com sucesso!");
        } else if ("ERROR".equals(status)) {
            return ResponseDTO.err("Houve um erro, empresa não cadastrada, tente mais tarde!");
        }
        return null;
    }

    @GetMapping("/find-usuarios-by-uuid-empresa/{uuid}")
    public List<EmpresaUsuarioDTO> findUsuariosByUuidEmpresa(@PathVariable String uuid){
        return this.empresaUsuarioService.findUsuariosByUuidEmpresa(uuid);
    }
}
