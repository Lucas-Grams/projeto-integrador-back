package br.com.pnipapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record EmbarcacaoTRFormDTO(

    Long id,
    @NotNull(message = "Embarcação é obrigatório.")
    Long idEmbarcacao,
    @NotBlank(message = "Campo nome proprietário é obrigatório.")
    String nomeProprietario,
    @NotBlank(message = "Campo cpf proprietário é obrigatório.")
    String cpfProprietario,
    @NotBlank(message = "Campo email proprietário é obrigatório.")
    String emailProprietario,
    @NotBlank(message = "Campo declaracao proprietário é obrigatório.")
    String declaracaoProprietarioBase64,
    String declaracaoProprietario,
    @NotBlank(message = "Campo mercado atuação é obrigatório.")
    String mercadoAtuacao,
    @NotBlank(message = "Campo tempo médio pesca é obrigatório.")
    Integer tempoMedioPesca,
    @NotBlank(message = "Campo tipo conservação é obrigatório.")
    String tipoConservacao,
    @NotBlank(message = "Campo capacidade total é obrigatório.")
    Integer capacidadeTotal,
    @NotBlank(message = "Campo capacidade pescado é obrigatório.")
    Integer capacidadePescado

) {}
