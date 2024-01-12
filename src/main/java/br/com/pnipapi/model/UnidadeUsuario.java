package br.com.pnipapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "unidade_usuario", schema = "public")
@Entity
public class UnidadeUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "unidade_usuario",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_unidade")
    )
    private List<Unidade> unidades;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "unidade_usuario",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<Usuario> usuarios;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "unidade_usuario",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_permissao")
    )
    private List<Permissao> permissoes;

}
