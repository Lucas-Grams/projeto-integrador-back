package br.com.pnipapi.controller;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.service.UnidadeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidade")
public class UnidadeController {
    UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService){ this.unidadeService = unidadeService; }

    @PostMapping("/salvar")
    public ResponseDTO<Unidade> save(@RequestBody UnidadeFormDTO unidade){
        return unidadeService.save(unidade);
    }

    @GetMapping("/findAll")
    public List<Unidade> findAll(){
        return unidadeService.findAll();
    }

    @GetMapping("/getGerenciadoras/{tipo}")
    public List<Unidade> getGerenciadoras(@PathVariable String tipo) {return unidadeService.getGerenciadoras(tipo);}

    @PostMapping("excluirUnidade")
    public void delete(@RequestBody String uuid){
        unidadeService.delete(uuid);
    }

    @GetMapping("findUnidadeByUuid/{uuid}")
    public Unidade findUnidadeByUuid(@PathVariable String uuid){
        return this.unidadeService.findByUuid(uuid);
    }

    @PostMapping("/update")
    public ResponseDTO<Unidade> update(@RequestBody UnidadeFormDTO unidade){
        return unidadeService.update(unidade);
    }
}
