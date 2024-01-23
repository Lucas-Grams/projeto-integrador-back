package br.com.pnipapi.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmbarcacaoTRDTO {

    private Long id;
    private String uuid;
    private EmbarcacaoDTO embarcacaoDTO;
    private Long idUsuario;
    private String nomeProprietario;
    private String cpfProprietario;
    private String emailProprietario;
    private String declaracaoProprietario;
    private String mercadoAtuacao;
    private Integer tempoMedioPesca;
    private String tipoConservacao;
    private Integer capacidadeTotal;
    private Integer capacidadePescado;
    private Boolean ativo;
    private String dataCriacao;

}
