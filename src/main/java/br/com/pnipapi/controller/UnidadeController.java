package br.com.pnipapi.controller;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.dto.UnidadeUsuarioDTO;
import br.com.pnipapi.model.TipoUnidade;
import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.service.UnidadeService;
import br.com.pnipapi.service.UnidadeUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidade")
public class UnidadeController {
    UnidadeService unidadeService;
    UnidadeUsuarioService unidadeUsuarioService;

    public UnidadeController(UnidadeService unidadeService, UnidadeUsuarioService unidadeUsuarioService)
    {
        this.unidadeService = unidadeService;
        this.unidadeUsuarioService = unidadeUsuarioService;
    }

    @PostMapping("/salvar")
    public ResponseDTO save(@RequestBody List<UnidadeUsuarioDTO> unidadeUsuarios){
        return unidadeService.saveUnidadeUsuario(unidadeUsuarios);
    }

    @GetMapping("/findAll")
    public List<Unidade> findAll(){
        return unidadeService.findAll();
    }

    @GetMapping("/getGerenciadoras/{tipo}")
    public List<Unidade> getGerenciadoras(@PathVariable String tipo) {return unidadeService.getGerenciadoras(tipo);}

    @PostMapping("inativarUnidade")
    public void inativa(@RequestBody String uuid){
        unidadeService.inativa(uuid);
    }

    @GetMapping("findUnidadeByUuid/{uuid}")
    public Unidade findUnidadeByUuid(@PathVariable String uuid){
        return this.unidadeService.findByUuid(uuid);
    }

    @PostMapping("/update")
    public ResponseDTO<Unidade> update(@RequestBody UnidadeFormDTO unidade){
        return unidadeService.update(unidade);
    }

    @GetMapping("/findAllTipos")
    public ResponseDTO<List<TipoUnidade>> findAllTipos(){return this.unidadeService.findAllTipos();}

    @GetMapping("/findUsuariosByUnidadeUuid/{uuid}")
    public List<UnidadeUsuarioDTO> findUsuariosByUnidadeUuid(@PathVariable String uuid){return unidadeUsuarioService.findUsuariosByUnidadeUuid(uuid);}


}
