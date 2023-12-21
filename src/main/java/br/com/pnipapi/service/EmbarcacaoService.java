package br.com.pnipapi.service;

import br.com.pnipapi.dto.EmbarcacaoDTO;
import br.com.pnipapi.model.Embarcacao;
import br.com.pnipapi.repository.EmbarcacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbarcacaoService {

    EmbarcacaoRepository embarcacaoRepository;

    public EmbarcacaoService(EmbarcacaoRepository embarcacaoRepository) {
        this.embarcacaoRepository = embarcacaoRepository;
    }

    public List<EmbarcacaoDTO> findAllEmbarcacaoByRgpTieNome(String filter) {
        List<Embarcacao> embarcacoes = embarcacaoRepository.findAllEmbarcacaoByRgpTieNome(filter);

        return embarcacoes.stream().map(embarcacao -> {
            String frota = embarcacaoRepository.findFrotaByIdEmbarcacao(embarcacao.getId());

            return new EmbarcacaoDTO(embarcacao.getId(), embarcacao.getNome(), embarcacao.getNumMarinhaTie(),
                embarcacao.getNumMarinha(), embarcacao.getNumRgp(), embarcacao.getUf(), embarcacao.getPais(),
                embarcacao.getAnoConstrucao(), embarcacao.getHp(), embarcacao.getComprimento(),
                embarcacao.getPetrecho(), embarcacao.getCodigoIn(), frota, null, null);
        }).toList();

    }

}
