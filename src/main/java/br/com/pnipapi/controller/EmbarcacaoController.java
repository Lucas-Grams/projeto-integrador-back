package br.com.pnipapi.controller;

import br.com.pnipapi.dto.*;
import br.com.pnipapi.service.EmbarcacaoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/embarcacao")
public class EmbarcacaoController {

    EmbarcacaoService embarcacaoService;

    public EmbarcacaoController(EmbarcacaoService embarcacaoService) {
        this.embarcacaoService = embarcacaoService;
    }

    @GetMapping("/buscar/{param}")
    ResponseDTO<List<EmbarcacaoDTO>> findAllEmbarcacaoByRgpTieNome(@PathVariable String param) {
        if (!StringUtils.hasText(param)) {
            return ResponseDTO.err("Parâmetro de busca obrigatório.");
        }
        if (param.length() < 3) {
            return ResponseDTO.err("Informe pelo menos 3 caracteres.");
        }
        return ResponseDTO.ok(embarcacaoService.findAllEmbarcacaoByRgpTieNome(param));
    }

    @PostMapping("/tr")
    ResponseDTO<String> cadastrarEmbarcacaoTR(@RequestBody @Valid List<EmbarcacaoTRFormDTO> embarcacoesDTO) {
        if (CollectionUtils.isEmpty(embarcacoesDTO)) {
            return ResponseDTO.err("Informe pelo menos uma embarcação.");
        }
        return ResponseDTO.ok(embarcacaoService.cadastrarEmbarcacao(embarcacoesDTO));
    }

    @GetMapping("/tr/minhas-embarcacoes")
    List<EmbarcacaoTRDTO> minhasEmbarcacoes() {
        return embarcacaoService.minhasEmbarcacoes();
    }

    @GetMapping("/tr/detalhes-embarcacao/{uuid}")
    EmbarcacaoTRDTO detalhesEmbarcacao(@PathVariable String uuid) {
        return embarcacaoService.detalhesEmbarcacao(uuid);
    }

    @GetMapping(value = "/tr/download/declaracao-proprietario/{uuid}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadDeclaracaoProprietario(@PathVariable String uuid) {
        return ResponseEntity.of(Optional.of(embarcacaoService.downloadDeclaracaoProprietario(uuid)));
    }

}
