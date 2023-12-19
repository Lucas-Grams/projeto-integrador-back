package br.com.pnipapi.dto;


import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FinalizarSolicitacaoDTO {

    UUID uuidSolicitacao;
    String statusSolicitacao;
    String msgSolicitacao;
    List<VinculoEmbarcacao> embarcacoes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class VinculoEmbarcacao {
        Long id;
        Boolean aprovado;
    }

}
