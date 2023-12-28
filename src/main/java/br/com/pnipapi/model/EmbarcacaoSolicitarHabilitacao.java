package br.com.pnipapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "embarcacao_solicitar_habilitacao", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class EmbarcacaoSolicitarHabilitacao {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "id_embarcacao")
    private Long idEmbarcacao;

    @Basic
    @Column(name = "id_solicitacao")
    private Long idSolicitacao;

    @Basic
    @Column(name = "aprovado")
    private Boolean aprovado;

    public EmbarcacaoSolicitarHabilitacao(Long idEmbarcacao, Long idSolicitacao, Boolean aprovado) {
        this.idEmbarcacao = idEmbarcacao;
        this.idSolicitacao = idSolicitacao;
        this.aprovado = aprovado;
    }
}
