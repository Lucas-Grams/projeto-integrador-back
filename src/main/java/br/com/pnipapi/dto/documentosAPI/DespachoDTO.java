package br.com.pnipapi.dto.documentosAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DespachoDTO {
    private String uuid;
    private String codigoDescricao;
    private Boolean pendente;
    private Timestamp criadoEm;
    private Integer usuarioAcao;
    private Integer ordemProcesso;
    private String kind;
    private InclusaoDTO inclusao;
    private TramitacaoDTO tramitacao;
}
