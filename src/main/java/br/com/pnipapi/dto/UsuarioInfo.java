package br.com.pnipapi.dto;

import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.Usuario;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
public record UsuarioInfo(Long id, String cpf, String email, String nome, Date dataCadastro, Date ultimoAcesso, UUID uuid, boolean ativo, List<Permissao> permissoes) {
}
