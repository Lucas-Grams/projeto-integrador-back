package br.com.pnipapi.dto;

import br.com.pnipapi.model.Usuario;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.List;

public record UnidadeFormDTO(
   Long id,
   @NotBlank(message = "Campo nome é obrigatório.")
   @Max(value = 100, message = "Campo nome não poder mais de 100 caracteres.")
   String nome,
   @NotBlank(message = "Campo tipo é obrigatório.")
   @Max(value = 100, message = "Campo tipo não poder mais de 100 caracteres.")
   String tipo,
   @NotBlank(message = "Campo unidade gerenciadora é obrigatório.")
   Long idUnidadeGerenciadora,
   @NotBlank(message = "Campo CEP é obrigatório.")
   String cep,
   @NotBlank(message = "Campo rua é obrigatório.")
   String rua,
   String numero,
   String bairro,
   String complemento,
   @NotBlank(message = "Campo cidade é obrigatório.")
   String cidade,
   @NotBlank(message = "Campo UF é obrigatório.")
   String uf,
   @NotBlank(message = "Campo latitude é obrigatório.")
   String latitude,
   @NotBlank(message = "Campo longitude é obrigatório.")
   String longitude,
   Usuario usuarioRepresentante
) {}
