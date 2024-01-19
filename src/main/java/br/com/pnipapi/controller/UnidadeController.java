package br.com.pnipapi.controller;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeUsuarioDTO;
import br.com.pnipapi.model.TipoUnidade;
import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.service.UnidadeService;
import br.com.pnipapi.service.UnidadeUsuarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/unidade")
public class UnidadeController {
    UnidadeService unidadeService;
    UnidadeUsuarioService unidadeUsuarioService;

    public UnidadeController(UnidadeService unidadeService, UnidadeUsuarioService unidadeUsuarioService) {
        this.unidadeService = unidadeService;
        this.unidadeUsuarioService = unidadeUsuarioService;
    }

    @PostMapping("/salvar")
    public ResponseDTO save(@RequestBody List<UnidadeUsuarioDTO> unidadeUsuarios){
        String status = unidadeService.saveUnidadeUsuario(unidadeUsuarios);
        if("OK".equals(status)) {
            return ResponseDTO.ok("Unidade cadastrada com sucesso!");
        } else if ("ERROR".equals(status)) {
            return ResponseDTO.err("Houve um problema no servidor, tente mais tarde.");
        }else {
            return null;
        }
    }

    @GetMapping("/find-all")
    public List<Unidade> findAll(){
        return unidadeService.findAll();
    }

    @GetMapping("/get-gerenciadoras/{tipo}")
    public List<Unidade> getGerenciadoras(@PathVariable String tipo) {return unidadeService.getGerenciadoras(tipo);}

    @PostMapping("inativar-unidade")
    public void inativa(@RequestBody String uuid){
        unidadeService.inativa(uuid);
    }

    @GetMapping("find-unidade-by-uuid/{uuid}")
    public Unidade findUnidadeByUuid(@PathVariable String uuid){
        return this.unidadeService.findByUuid(uuid);
    }

    @GetMapping("/find-all-tipos")
    public ResponseDTO<List<TipoUnidade>> findAllTipos(){return this.unidadeService.findAllTipos();}

    @GetMapping("/find-usuarios-by-unidade-uuid/{uuid}")
    public List<UnidadeUsuarioDTO> findUsuariosByUnidadeUuid(@PathVariable String uuid){return unidadeUsuarioService.findUsuariosByUnidadeUuid(uuid);}


}
