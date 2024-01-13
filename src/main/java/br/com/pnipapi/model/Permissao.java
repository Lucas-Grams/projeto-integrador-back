package br.com.pnipapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Table(name = "permissao", schema = "public")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Permissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="descricao")
    private String descricao;

    public Permissao(String descricao) {
        this.descricao = descricao.toLowerCase();
    }
}
