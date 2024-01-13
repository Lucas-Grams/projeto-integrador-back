package br.com.pnipapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Permissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private int id;
    private String descricao;

    public Permissao(String descricao) {
        this.descricao = descricao.toLowerCase();
    }
}
