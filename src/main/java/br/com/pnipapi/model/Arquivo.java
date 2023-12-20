package br.com.pnipapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "arquivo", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Arquivo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "nome")
    private String nome;

    @Basic
    @Column(name = "caminho")
    private String caminho;

    @Basic
    @Column(name = "origem_arquivo")
    private String origemArquivo;

    @Basic
    @Column(name = "tamanho")
    private Long tamanho;

    @Basic
    @Column(name = "tipo")
    private String tipo;

}
