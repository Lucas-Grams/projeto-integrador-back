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

    @Basic
    @Column(name = "modalidade_pesca_complementar", nullable = true, length = -1)
    private String modalidadePescaComplementar;

    @Basic
    @Column(name = "modalidade_pesca_principal", nullable = true, length = -1)
    private String modalidadePescaPrincipal;

    @Basic
    @Column(name = "area_operacao", nullable = true, length = -1)
    private String areaOperacao;

    @Basic
    @Column(name = "especie_alvo", nullable = true, length = -1)
    private String especieAlvo;

}
