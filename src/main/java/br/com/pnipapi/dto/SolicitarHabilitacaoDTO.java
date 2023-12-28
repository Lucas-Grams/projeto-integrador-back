package br.com.pnipapi.dto;

import br.com.pnipapi.dto.documentosAPI.DespachoDTO;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitarHabilitacaoDTO {

    DespachoDTO despachoDTO;
    HabilitarTRDTO habilitarTRDTO;
}
