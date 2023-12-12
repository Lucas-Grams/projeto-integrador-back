package br.com.pnipapi.dto.documentosAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusTramitacaoDTO {
    private String codigo;
    private String descricao;
}
