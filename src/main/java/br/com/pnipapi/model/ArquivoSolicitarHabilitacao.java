package br.com.pnipapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "arquivo_solicitar_habilitacao", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoSolicitarHabilitacao {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "id_arquivo")
    private Long idArquivo;

    @Basic
    @Column(name = "id_solicitacao")
    private Long idSolicitacao;

    @Basic
    @Column(name = "id_embarcacao")
    private Long idEmbarcacao;

    public ArquivoSolicitarHabilitacao(Long idArquivo, Long idSolicitacao, Long idEmbarcacao) {
        this.idArquivo = idArquivo;
        this.idSolicitacao = idSolicitacao;
        this.idEmbarcacao = idEmbarcacao;
    }

    public ArquivoSolicitarHabilitacao(Long idArquivo, Long idSolicitacao) {
        this.idArquivo = idArquivo;
        this.idSolicitacao = idSolicitacao;
    }
}
