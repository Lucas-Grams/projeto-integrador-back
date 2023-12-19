package br.com.pnipapi.controller;

import br.com.pnipapi.dto.FinalizarSolicitacaoDTO;
import br.com.pnipapi.dto.HabilitarTRDTO;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.SolicitacaoHabilitacaoDTO;
import br.com.pnipapi.model.SolicitarHabilitacao;
import br.com.pnipapi.service.TRService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/find/solicitacao/by/uuid/{uuid}")
    ResponseDTO<SolicitarHabilitacao> findSolicitacaoByUuid(@PathVariable UUID uuid) {
        if (uuid == null) {
            return ResponseDTO.err("Parâmetro de busca obrigatório.");
        }
        return ResponseDTO.ok(trService.findSolicitacaoByUuid(uuid));
    }

    @GetMapping("/find/all/solicitacoes")
    ResponseDTO<List<SolicitarHabilitacao>> findAll() {
        return ResponseDTO.ok(trService.findAll());
    }

    @GetMapping("/find/solicitacoes/by/status/{status}")
    ResponseDTO<List<SolicitarHabilitacao>> findSolicitacoesByStatus(@PathVariable String status) {
        return ResponseDTO.ok(trService.findSolicitacoesByStatus(status));
    }

    @PostMapping("/finalizar-solicitacao")
    ResponseDTO<String> finalizarSolicitacao(@RequestBody FinalizarSolicitacaoDTO finalizarSolicitacaoDTO) {
        return ResponseDTO.ok(trService.finalizarSolicitacao(finalizarSolicitacaoDTO));
    }

}
