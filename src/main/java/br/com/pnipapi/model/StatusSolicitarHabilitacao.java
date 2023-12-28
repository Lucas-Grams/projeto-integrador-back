package br.com.pnipapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "status_solicitar_habilitacao", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class StatusSolicitarHabilitacao {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "id_status")
    private Long idStatus;

    @Basic
    @Column(name = "id_solicitacao")
    private Long idSolicitacao;

    public StatusSolicitarHabilitacao(Long idStatus, Long idSolicitacao) {
        this.idStatus = idStatus;
        this.idSolicitacao = idSolicitacao;
    }
}
