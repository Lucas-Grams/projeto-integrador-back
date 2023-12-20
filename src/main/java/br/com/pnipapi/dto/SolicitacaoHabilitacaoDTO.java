package br.com.pnipapi.dto;

public record SolicitacaoHabilitacaoDTO(
    Long id,
    Long idUsuario,
    String uuid,
    String dataSolicitacao) {
}
