package br.com.pnipapi.controller;

import br.com.pnipapi.dto.EmbarcacaoDTO;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.service.EmbarcacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/embarcacao")
public class EmbarcacaoController {

    EmbarcacaoService embarcacaoService;

    public EmbarcacaoController(EmbarcacaoService embarcacaoService) {
        this.embarcacaoService = embarcacaoService;
    }

    @GetMapping("/buscar/{param}")
    ResponseDTO<List<EmbarcacaoDTO>> findAllEmbarcacaoByRgpTieNome(@PathVariable String param) {
        return ResponseDTO.ok(embarcacaoService.findAllEmbarcacaoByRgpTieNome(param));
    }

}
