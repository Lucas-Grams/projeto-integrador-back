package br.com.pnipapi.model;

import br.com.pnipapi.dto.UnidadeFormDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.Cascade;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "unidade", schema = "public")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipo;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(nullable = false, name = "id_endereco")
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "id_unidade_gerenciadora")
    private Unidade unidadeGerenciadora;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private Date dataCadastro = new Date(System.currentTimeMillis());

    @Column
    private Date ultimaAtualizacao;

    @Transient
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}  )
    @JoinTable(
        name="unidade_usuario",
        joinColumns = @JoinColumn(name = "id_unidade"),
        inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<Usuario> usuarios;

    public Unidade toUnidade(Unidade uni){
        Unidade unidadeNova = new Unidade();
        if(uni.getId() != null){
            unidadeNova.setId(uni.getId());
        }
        unidadeNova.setNome(uni.getNome());
        unidadeNova.setTipo(uni.getTipo());
        unidadeNova.setAtivo(true);
        unidadeNova.setUnidadeGerenciadora(uni.getUnidadeGerenciadora());
        unidadeNova.setDataCadastro(Date.valueOf(LocalDate.now()));

        Endereco endereco = new Endereco();
        endereco.setUf(uni.endereco.getUf());
        endereco.setCidade(uni.endereco.getCidade());
        endereco.setBairro(uni.endereco.getBairro());
        endereco.setRua(uni.endereco.getRua());
        endereco.setComplemento(uni.endereco.getComplemento());
        endereco.setNumero(uni.endereco.getNumero());
        endereco.setCep(uni.endereco.getCep());
        endereco.setLatitude(uni.endereco.getLatitude());
        endereco.setLongitude(uni.endereco.getLongitude());
        unidadeNova.setEndereco(endereco);
        return unidadeNova;
    }
}
