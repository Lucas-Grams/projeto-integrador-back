package br.com.pnipapi.controller;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeUsuarioDTO;
import br.com.pnipapi.dto.UsuarioInfo;
import br.com.pnipapi.model.UnidadeUsuario;
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

    @GetMapping("/findByUuid/{uuid}")
    public Usuario findByUuid(@PathVariable String uuid){return usuarioService.findByUuid(uuid);}

    @GetMapping("/findUsuariosUnidade/{uuid}")
    public List<Usuario> findUsuariosUnidade(@PathVariable String uuid){return usuarioService.findUsuariosUnidade(uuid);}

    @GetMapping("/findUsuariosDip")
    public List<Usuario> findUsuariosDip(){return usuarioService.findUsuariosDip();}

    @GetMapping("findUnidadesByUsuarioUuid/{uuid}")
    public List<UnidadeUsuarioDTO> findUnidadesByUsuarioUuid(@PathVariable String uuid){return unidadeUsuarioService.findUnidadesByUsuarioUuid(uuid);}
}
