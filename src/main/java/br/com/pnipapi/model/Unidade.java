package br.com.pnipapi.model;

import br.com.pnipapi.dto.UnidadeFormDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "unidade", schema = "public")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipo;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_endereco")
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "id_unidade_gerenciadora")
    private Unidade unidadeGerenciadora;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private Date dataCadastro = new Date(System.currentTimeMillis());

    @Column
    private Date ultima_atualizacao;

    @OneToMany
    @JoinTable(
        name="unidade_usuario",
        joinColumns = @JoinColumn(name = "id_unidade"),
        inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    @Cascade(CascadeType.ALL)
    private List<Usuario> usuarios;



    public Unidade toUnidade(UnidadeFormDTO uni){
        Unidade unidadeNova = new Unidade();
        if(uni.id() != null){
            unidadeNova.setId(uni.id());
        }
        unidadeNova.setNome(uni.nome());
        unidadeNova.setTipo(uni.tipo());
        unidadeNova.setDataCadastro(Date.valueOf(LocalDate.now()));

        Endereco endereco = new Endereco();
        endereco.setUf(uni.uf());
        endereco.setCidade(uni.cidade());
        endereco.setBairro(uni.bairro());
        endereco.setRua(uni.rua());
        endereco.setComplemento(uni.complemento());
        endereco.setNumero(uni.numero());
        endereco.setCep(uni.cep());
        unidadeNova.setEndereco(endereco);

        return unidadeNova;
    }

}
