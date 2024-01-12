package br.com.pnipapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Table(name = "permissao", schema = "public")
@Entity
public class Permissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="descricao")
    private Permissoes descricao;

    @ManyToMany(mappedBy = "permissoes")
    private Set<UnidadeUsuario> unidadesUsuarios;

    public enum Permissoes{
        admin,
        tr,
        mpa,
        so,
        representante
    }
}
