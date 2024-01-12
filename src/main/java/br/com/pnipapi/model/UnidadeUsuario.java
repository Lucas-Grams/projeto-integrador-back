package br.com.pnipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Table(name = "unidade_usuario", schema = "public")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeUsuario {
    public UnidadeUsuario(Unidade unidade, Usuario usuario, Permissao permissao) {
        System.out.println(permissao.toString());
        System.out.println(usuario.toString());
        System.out.println(unidade.toString());
        this.unidade = unidade;
        this.usuario = usuario;
        this.permissao = permissao;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_unidade")
    private Unidade unidade;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_permissao")
    private Permissao permissao;

    @Column(name = "ativo")
    private boolean ativo = true;

}
