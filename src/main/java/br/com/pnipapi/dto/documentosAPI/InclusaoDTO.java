package br.com.pnipapi.dto.documentosAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InclusaoDTO {
    private Map<String, Object> metadados;
    private List<ArquivoAnexoDTO> arquivosAnexos = new ArrayList<>();
}
