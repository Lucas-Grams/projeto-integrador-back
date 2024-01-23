package br.com.pnipapi.dto;

import lombok.*;

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

}
