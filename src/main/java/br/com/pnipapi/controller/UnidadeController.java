package br.com.pnipapi.controller;

import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.service.UnidadeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/unidade")
public class UnidadeController {
    UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService){ this.unidadeService = unidadeService; }

    @PostMapping("/salvar")
    public ResponseDTO<Unidade> save(@RequestBody UnidadeFormDTO unidade){
        System.out.println("controller");
        System.out.println(unidade.nome());
        return unidadeService.save(unidade);
    }

    @GetMapping("/findAll")
    public List<Unidade> findAll(){
        return unidadeService.findAll();
    }

}
