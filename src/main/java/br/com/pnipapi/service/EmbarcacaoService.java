package br.com.pnipapi.service;

import br.com.pnipapi.repository.EmbarcacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class EmbarcacaoService {

    EmbarcacaoRepository embarcacaoRepository;

    public EmbarcacaoService(EmbarcacaoRepository embarcacaoRepository) {
        this.embarcacaoRepository = embarcacaoRepository;
    }

}
