package br.com.pnipapi.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
public record EmbarcacaoDTO (
    Long id,
    @NotNull @NotEmpty @Length(max = 100)
    String nome,
    @NotNull @NotEmpty @Length(max = 70)
    String numMarinhaTie,
    @NotNull @NotEmpty @Length(max = 70)
    String numMarinha,
    @NotNull @NotEmpty @Length(max = 70)
    String numRgp,
    @NotNull @NotEmpty
    String uf,
    @NotNull @NotEmpty
    String pais,
    Integer anoConstrucao,
    Integer hp,
    String comprimento,
    @NotNull @NotEmpty
    String petrecho,
    @NotNull @NotEmpty
    String codigoIn) {
}
