package br.com.pnipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Data
@Table(name = "unidade_usuario", schema = "public")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeUsuario {
    public UnidadeUsuario(Unidade unidade, Usuario usuario, Permissao permissao, boolean ativo) {
        this.unidade = unidade;
        this.usuario = usuario;
        this.permissao = permissao;
        this.ativo = ativo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_unidade")
    private Unidade unidade;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_permissao")
    private Permissao permissao;

    @Column(name = "ativo")
    private boolean ativo;

}
