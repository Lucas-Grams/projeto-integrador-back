package br.com.pnipapi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "frota", schema = "public")
public class Frota {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "codigo", nullable = true, length = -1)
    private String codigo;

    @Basic
    @Column(name = "descricao", nullable = true, length = -1)
    private String descricao;
}
