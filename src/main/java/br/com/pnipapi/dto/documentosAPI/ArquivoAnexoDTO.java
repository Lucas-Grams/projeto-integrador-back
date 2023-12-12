package br.com.pnipapi.dto.documentosAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArquivoAnexoDTO {
    private String tipo;
    private String nome;
    private long tamanho;
    private String descricao;
    private String caminho;
    private byte[] conteudo;
}
