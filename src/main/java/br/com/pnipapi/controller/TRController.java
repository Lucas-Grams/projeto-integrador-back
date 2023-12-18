package br.com.pnipapi.controller;

import br.com.pnipapi.dto.HabilitarTRDTO;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.SolicitacaoHabilitacaoDTO;
import br.com.pnipapi.service.TRService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tr")
public class TRController {

    TRService trService;

    public TRController(TRService trService) {
        this.trService = trService;
    }

    @GetMapping("/minhas-solicitacoes")
    List<SolicitacaoHabilitacaoDTO> minhasSolicitacoes() {
        return trService.minhasSolicitacoes();
    }

    @GetMapping("/detalhes-solicitacao/{uuid}")
    HabilitarTRDTO detalhesSolicitacao(@PathVariable String uuid) {
        return trService.detalhesSolicitacao(uuid);
    }

    @PostMapping("/solicitar-habilitacao")
    ResponseDTO<String> solicitarHabilitacao(@RequestBody HabilitarTRDTO habilitarTRDTO) {
        return ResponseDTO.ok(trService.solicitarHabilitacao(habilitarTRDTO));
    }

}
