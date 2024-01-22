package br.com.pnipapi.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmbarcacaoDTO {

    private Long id;
    @NotNull @NotEmpty @Length(max = 100)
    private String nome;
    @NotNull @NotEmpty @Length(max = 70)
    private String numMarinhaTie;
    @NotNull @NotEmpty @Length(max = 70)
    private String numMarinha;
    @NotNull @NotEmpty @Length(max = 70)
    private String numRgp;
    @NotNull @NotEmpty
    private String uf;
    @NotNull @NotEmpty
    private String pais;
    private Integer anoConstrucao;
    private Integer hp;
    private String comprimento;
    @NotNull @NotEmpty
    private String petrecho;
    @NotNull @NotEmpty
    private String codigoIn;
    private String frota;
    private String tipoCertificacao;

}
