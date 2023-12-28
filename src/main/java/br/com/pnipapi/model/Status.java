package br.com.pnipapi.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "status", schema = "public")
public class Status {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "descricao")
    private String descricao;

}
