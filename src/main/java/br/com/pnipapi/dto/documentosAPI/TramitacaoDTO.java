package br.com.pnipapi.dto.documentosAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TramitacaoDTO {
    private String mensagem;
    private Integer unidadeDestino;
    private StatusTramitacaoDTO statusTramitacao;
}
