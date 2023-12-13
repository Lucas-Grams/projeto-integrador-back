package br.com.pnipapi.controller;

import br.com.pnipapi.dto.HabilitarTRDTO;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.service.TRService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tr")
public class TRController {

    TRService trService;

    public TRController(TRService trService) {
        this.trService = trService;
    }

    @PostMapping("/solicitar-habilitacao")
    ResponseDTO<String> solicitarHabilitacao(@RequestBody HabilitarTRDTO habilitarTRDTO) {
        return ResponseDTO.ok(trService.solicitarHabilitacao(habilitarTRDTO));
    }

}
