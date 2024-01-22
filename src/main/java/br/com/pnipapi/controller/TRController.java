package br.com.pnipapi.controller;

import br.com.pnipapi.dto.FinalizarSolicitacaoDTO;
import br.com.pnipapi.dto.HabilitarTRDTO;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.SolicitacaoHabilitacaoDTO;
import br.com.pnipapi.model.SolicitarHabilitacao;
import br.com.pnipapi.service.TRService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
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
    ResponseDTO<List<SolicitarHabilitacao>> findSolicitacoesByStatus(@PathVariable List<String> status) {
        return ResponseDTO.ok(trService.findSolicitacoesByStatus(status));
    }

    @GetMapping("/find/status/ultima/solicitacao")
    ResponseDTO<String> findStatusUltimaSolicatacao() {
        return ResponseDTO.ok(trService.findStatusUltimaSolicatacao());
    }

    @PostMapping("/finalizar-solicitacao")
    ResponseDTO<String> finalizarSolicitacao(@RequestBody FinalizarSolicitacaoDTO finalizarSolicitacaoDTO) {
        return ResponseDTO.ok(trService.finalizarSolicitacao(finalizarSolicitacaoDTO));
    }

    @GetMapping(value = "/download/anexos/{uuid}/{nome}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadAnexo(@PathVariable String uuid, @PathVariable String nome) {
        return ResponseEntity.of(Optional.of(trService.downloadAnexo(uuid, nome)));
    }

    @PostMapping("/desvincular-embarcacao")
    ResponseDTO<String> desvincularEmbarcacao(@RequestBody String uuid) {
        return ResponseDTO.ok(trService.desvincularEmbarcacao(uuid));
    }

}
