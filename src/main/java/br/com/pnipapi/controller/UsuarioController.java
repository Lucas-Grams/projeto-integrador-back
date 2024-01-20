package br.com.pnipapi.controller;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeUsuarioDTO;
import br.com.pnipapi.dto.UsuarioInfo;
import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.service.UnidadeUsuarioService;
import br.com.pnipapi.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    UsuarioService usuarioService;
    UnidadeUsuarioService unidadeUsuarioService;

    public UsuarioController(UsuarioService usuarioService, UnidadeUsuarioService unidadeUsuarioService) {
        this.usuarioService = usuarioService;
        this.unidadeUsuarioService = unidadeUsuarioService;
    }

    @PostMapping()
    public ResponseDTO<UsuarioInfo> salvar(@RequestBody @Valid Usuario usuario) {
        return usuarioService.salvar(usuario);
    }

    @GetMapping()
    public List<UsuarioInfo> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/find-by-uuid/{uuid}")
    public Usuario findUsuarioByUuid(@PathVariable String uuid){return usuarioService.findUsuarioByUuid(uuid);}

    @GetMapping("/find-usuarios-unidade/{uuid}")
    public List<Usuario> findUsuariosUnidade(@PathVariable String uuid){return usuarioService.findUsuariosUnidade(uuid);}

    @GetMapping("/find-usuarios-dip")
    public List<Usuario> findUsuariosDip(){return usuarioService.findUsuariosDip();}

    @GetMapping("/find-unidades-by-usuario-uuid/{uuid}")
    public List<UnidadeUsuarioDTO> findUnidadesByUsuarioUuid(@PathVariable String uuid){return unidadeUsuarioService.findUnidadesByUsuarioUuid(uuid);}

    @PostMapping("/salvar-usuario")
    public ResponseDTO save(@RequestBody List<UnidadeUsuarioDTO> unidadeUsuarios){
        String status = usuarioService.saveUsuarioUnidade(unidadeUsuarios);
        if("OK".equals(status)){
            return ResponseDTO.ok("Usuário cadastrado com sucesso!");
        } else if ("ERROR".equals(status)) {
            return ResponseDTO.err("Houve um erro, usuário não cadastrado, tente mais tarde!");
        }
        return null;
    }

    @PostMapping("/ativa-inativa")
    public ResponseDTO ativaInativa(@RequestBody String uuid) {
        String status = usuarioService.ativaInativa(uuid);
        if ("OK".equals(status)) {
            return ResponseDTO.ok("Usuário cadastrado com sucesso!");
        } else if ("ERROR".equals(status)) {
            return ResponseDTO.err("Houve um erro, usuário não cadastrado, tente mais tarde!");
        }
        return null;
    }
}
