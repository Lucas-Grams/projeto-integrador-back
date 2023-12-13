package br.com.pnipapi.dto.documentosAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoDTO {
    private String uuid;
    private StatusProcesso statusProcesso;
    private Timestamp criadoEm;
    private String numeroProcesso;
    private String tipoProcesso;
    private Boolean finalizado;
    private List<DespachoDTO> despachos = new ArrayList<>();
    private int usuarioId;

    public ProcessoDTO(String tipoProcesso, int usuarioId) {
        this.tipoProcesso = tipoProcesso;
        this.usuarioId = usuarioId;
    }
}
