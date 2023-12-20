package br.com.pnipapi.controller;

import br.com.pnipapi.dto.FinalizarSolicitacaoDTO;
import br.com.pnipapi.dto.HabilitarTRDTO;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.SolicitacaoHabilitacaoDTO;
import br.com.pnipapi.model.SolicitarHabilitacao;
import br.com.pnipapi.service.TRService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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

    @PostMapping("/upload/anexos/{uuid}")
    public ResponseEntity<String> uploadAnexosSolicitacao(@PathVariable String uuid,
        @RequestParam("arquivos") List<MultipartFile> arquivos,
        @RequestParam("embarcacoes") List<Long> idsEmbarcacao) {
        return ResponseEntity.ok(trService.uploadAnexosSolicitacao(uuid, idsEmbarcacao, arquivos));
    }

    @GetMapping("/download/anexos/{uuid}/{nome}")
    public ResponseEntity<StreamingResponseBody> downloadAnexo(@PathVariable String uuid, @PathVariable String nome) {
        return ResponseEntity.ok(trService.downloadAnexo(uuid, nome, null));
    }

    @GetMapping("/download/anexos/{uuid}/{idEmbarcacao}")
    public ResponseEntity<StreamingResponseBody> downloadAnexo(@PathVariable String uuid,
        @PathVariable Long idEmbarcacao) {
        return ResponseEntity.ok(trService.downloadAnexo(uuid, null, idEmbarcacao));
    }

}
