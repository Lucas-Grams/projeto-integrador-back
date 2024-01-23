package br.com.pnipapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "embarcacao_tr", schema = "public")
public class EmbarcacaoTR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(updatable = false, nullable = false, unique = true)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "id_embarcacao", nullable = false)
    private Embarcacao embarcacao;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String nomeProprietario;

    @Column(nullable = false)
    private String cpfProprietario;

    @Column(nullable = false)
    private String emailProprietario;

    @ManyToOne
    @JoinColumn(name = "id_arquivo_declaracao_proprietario", nullable = false)
    private Arquivo declaracaoProprietario;

    @Column(nullable = false)
    private String mercadoAtuacao;

    @Column(nullable = false)
    private Integer tempoMedioPesca;

    @Column(nullable = false)
    private String tipoConservacao;

    @Column(nullable = false)
    private Integer capacidadeTotal;

    @Column(nullable = false)
    private Integer capacidadePescado;

    @Column(nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dataCriacao;

}
