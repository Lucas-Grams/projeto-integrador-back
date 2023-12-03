package br.com.pnipapi.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Data
@Table(name = "unidade", schema = "public")
@Entity
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipo;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_endereco")
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "id_unidade_gerenciadora")
    private Unidade unidadeGerenciadora;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private Date dataCadastro = new Date(System.currentTimeMillis());

    @Column
    private Date ultima_atualizacao;

}
