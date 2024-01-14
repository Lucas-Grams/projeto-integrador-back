package br.com.pnipapi.controller;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UsuarioInfo;
import br.com.pnipapi.model.Usuario;
import br.com.pnipapi.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping()
    public ResponseDTO<UsuarioInfo> salvar(@RequestBody @Valid Usuario usuario) {
        return usuarioService.salvar(usuario);
    }

    @GetMapping()
    public List<UsuarioInfo> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/findUsuariosUnidade/{uuid}")
    public List<Usuario> findUsuariosUnidade(@PathVariable String uuid){return usuarioService.findUsuariosUnidade(uuid);}

    @GetMapping("/findUsuariosDip")
    public List<Usuario> findUsuariosDip(){return usuarioService.findUsuariosDip();}
}
