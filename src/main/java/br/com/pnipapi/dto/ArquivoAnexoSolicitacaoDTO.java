package br.com.pnipapi.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArquivoAnexoSolicitacaoDTO {

    private TipoAnexo tipoAnexo;
    private String nomeOriginal;
    private String nomeUpload;
    private String base64;
    private Long idEmbarcacao;

    public enum TipoAnexo {
        COPIA_HABILITACAO,
        DIPLOMA_CERTIFICADO,
        EMBARCACAO
    }

}
