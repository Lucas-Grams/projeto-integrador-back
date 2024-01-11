package br.com.pnipapi.model;

import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Table(name = "usuario", schema = "public")
@Entity
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cpf", nullable = false, length = 14, unique = true)
    @NotNull
    @Length(max = 14)
    @NotEmpty
    private String cpf;

    @Column(name = "email", nullable = false, unique = true)
    @NotNull
    @Email
    @NotEmpty
    private String email;

    @Column(name = "senha", nullable = false, length = 400)
    @Length(max = 400)
    private String senha;

    @NotNull
    @NotEmpty
    @Column(name = "nome", nullable = false, length = 100)
    @Length(max = 100)
    private String nome;

    @Column(name = "foto")
    private String foto;

    @Column(name = "data_cadastro", nullable = false)
    private Date dataCadastro;

    @Column(name = "ultimo_acesso")
    private Date ultimoAcesso;

    @Column(name = "ultima_atualizacao_cadastro")
    private Date ultimaAtualizacaoCadastro;

    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

//    @Type(type = "list-array")
//    @Column(name = "permissao", columnDefinition = "text[]")
@ElementCollection
@CollectionTable(
    name = "unidade_usuario",
    joinColumns = @JoinColumn(name = "id_usuario")
)
@Column(name = "permissao", columnDefinition = "text[]")
    private List<String> permissao;

//    @OneToMany
//    @JoinTable(
//        name="unidade_usuario",
//        joinColumns = @JoinColumn(name="id_usuario"),
//        inverseJoinColumns = @JoinColumn(name="id_permissao")
//    )
//    @Cascade(CascadeType.ALL)
//    private List<Permissao> permissao;

    public enum Permissao{
        admin,
        tr,
        mpa,
        so,
        representante
    }
}


