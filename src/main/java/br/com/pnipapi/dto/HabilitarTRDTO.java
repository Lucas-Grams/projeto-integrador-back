package br.com.pnipapi.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public record HabilitarTRDTO(Long id,
     @NotNull @NotEmpty @Length(max = 100)
     String nome,
     @NotNull @NotEmpty @Length(max = 70)
     String cpf,
     @NotNull @NotEmpty @Length(max = 70)
     String email,
     String telefone,
     @NotNull @NotEmpty
     String cep,
     String logradouro,
     String numero,
     String complemento,
     String municipio,
     String uf,
     @NotNull @NotEmpty
     String formacao,
     String numHabilitacao,
     @NotNull @NotEmpty
     String conselhoClasse,
     @NotNull @NotEmpty
     String ufConselho,
     @NotNull @NotEmpty
     String copiaHabilitacao,
     @NotNull @NotEmpty
     String diplomaCertificacao,
     @NotNull
     List<EmbarcacaoDTO> embarcacoes) {
}
