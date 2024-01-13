package br.com.pnipapi.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabilitarTRDTO {

    @NotNull @NotEmpty @Length(max = 100)
    private String nome;
    @NotNull @NotEmpty @Length(max = 70)
    private String cpf;
    @NotNull @NotEmpty @Length(max = 70)
    private String email;
    private String telefone;
    @NotNull @NotEmpty
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String municipio;
    private String uf;
    @NotNull @NotEmpty
    private String formacao;
    private String numHabilitacao;
    @NotNull @NotEmpty
    private String conselhoClasse;
    @NotNull @NotEmpty
    private String ufConselho;

    // nome original dos arquivos
    private String copiaHabilitacao;
    private String diplomaCertificacao;

    // arquivos em base64
    private String copiaHabilitacaoBase64;
    private String diplomaCertificacaoBase64;

    private List<EmbarcacaoDTO> embarcacoes;

}
