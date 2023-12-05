package br.com.pnipapi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "embarcacao", schema = "public")
public class Embarcacao {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "nome", nullable = true, length = 100)
    private String nome;

    @Basic
    @Column(name = "num_marinha_tie", nullable = true, length = 70)
    private String numMarinhaTie;

    @Basic
    @Column(name = "num_marinha", nullable = true, length = 70)
    private String numMarinha;

    @Basic
    @Column(name = "num_rgp", nullable = true, length = 50)
    private String numRgp;

    @Basic
    @Column(name = "sei_bphs", nullable = true, length = 50)
    private String seiBphs;

    @Basic
    @Column(name = "uf", nullable = true, length = 70)
    private String uf;

    @Basic
    @Column(name = "pais", nullable = true, length = 70)
    private String pais;

    @Basic
    @Column(name = "ano_construcao", nullable = true)
    private Integer anoConstrucao;

    @Basic
    @Column(name = "especie_alvo", nullable = true, length = 50)
    private String especieAlvo;

    @Basic
    @Column(name = "propulsao", nullable = true, length = 50)
    private String propulsao;

    @Basic
    @Column(name = "hp", nullable = true)
    private Integer hp;

    @Basic
    @Column(name = "combustivel", nullable = true, length = 50)
    private String combustivel;

    @Basic
    @Column(name = "comprimento", nullable = true)
    private Integer comprimento;

    @Basic
    @Column(name = "material_casco", nullable = true, length = 50)
    private String materialCasco;

    @Basic
    @Column(name = "canal_radio", nullable = true)
    private Integer canalRadio;

    @Basic
    @Column(name = "equipamento_comunicacao", nullable = true, length = 50)
    private String equipamentoComunicacao;

    @Basic
    @Column(name = "metodo_pesca", nullable = true, length = 50)
    private String metodoPesca;

    @Basic
    @Column(name = "area_pesca", nullable = true, length = 50)
    private String areaPesca;

    @Basic
    @Column(name = "zona_operacao", nullable = true, length = 50)
    private String zonaOperacao;

    @Basic
    @Column(name = "subvencao_oleo_diesel", nullable = true)
    private Boolean subvencaoOleoDiesel;

    @Basic
    @Column(name = "entidade_colaboradora", nullable = true, length = 50)
    private String entidadeColaboradora;

    @Basic
    @Column(name = "responsavel", nullable = true, length = 100)
    private String responsavel;

    @Basic
    @Column(name = "preps_ativo", nullable = true)
    private Boolean prepsAtivo;

    @Basic
    @Column(name = "arqueacao_bruta", nullable = true)
    private Integer arqueacaoBruta;

    @Basic
    @Column(name = "porto_origem", nullable = true, length = 70)
    private String portoOrigem;

    @Basic
    @Column(name = "porto_desemb", nullable = true, length = 70)
    private String portoDesemb;

    @Basic
    @Column(name = "embarcacao_arrendada", nullable = true)
    private Boolean embarcacaoArrendada;

    @Basic
    @Column(name = "empresa", nullable = true, length = 70)
    private String empresa;

    @Basic
    @Column(name = "capac_porao", nullable = true)
    private Integer capacPorao;

    @Basic
    @Column(name = "volume_tanque", nullable = true, precision = 0)
    private Double volumeTanque;

    @Basic
    @Column(name = "qtd_tripulacao", nullable = true)
    private Integer qtdTripulacao;

    @Basic
    @Column(name = "num_covos", nullable = true)
    private Integer numCovos;

    @Basic
    @Column(name = "petrecho", nullable = true, length = 70)
    private String petrecho;

    @Basic
    @Column(name = "codigo_in", nullable = true, length = 70)
    private String codigoIn;

}
