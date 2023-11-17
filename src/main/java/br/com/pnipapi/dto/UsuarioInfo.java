package br.com.pnipapi.dto;

import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record UsuarioInfo(Long id, String cpf, String email, String nome, Date dataCadastro, Date ultimoAcesso, UUID uuid, boolean ativo) {
}
