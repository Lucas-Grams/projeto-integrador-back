package br.com.pnipapi.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "solicitar_habilitacao", schema = "public")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class SolicitarHabilitacao {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Basic
    @Column(name = "uuid_solicitacao", nullable = true)
    private UUID uuidSolicitacao;

    @Basic
    @Column(name = "data_solicitacao", nullable = true)
    private Date dataSolicitacao;

    @Basic
    @Type(type = "jsonb")
    @Column(name = "metadado", columnDefinition = "jsonb")
    private String metadado;

}
