package br.com.pnipapi.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "usuario", schema = "public")
@Entity
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

    @ManyToMany
    @JoinTable(name="usuario_permissao", schema = "public",
        joinColumns = {@JoinColumn(name="id_usuario",referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name="id_permissao",referencedColumnName = "id")})
    private List<Permissao> permissoes;

    @OneToOne
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;
}
