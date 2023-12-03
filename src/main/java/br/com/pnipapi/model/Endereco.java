package br.com.pnipapi.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "endereco", schema = "public")
@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column
    private String rua;

    @Column
    private String cep;

    @Column
    private String numero;

    @Column
    private String complemento;

    @Column
    private String bairro;

    @Column
    private String cidade;

    @Column
    private String uf;

    @Column
    private String latitude;

    @Column
    private String longitude;

}
