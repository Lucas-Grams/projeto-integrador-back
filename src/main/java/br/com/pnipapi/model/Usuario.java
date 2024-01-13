package br.com.pnipapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Cascade;

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
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cpf", nullable = false, length = 14, unique = true)
    @NotNull
    @NotEmpty
    private String cpf;

    @Column(name = "email", nullable = false, unique = true)
    @NotNull
    @Email
    @NotEmpty
    private String email;

    @Column(name = "senha", nullable = false, length = 400)
    private String senha;

    @NotNull
    @NotEmpty
    @Column(name = "nome", nullable = false, length = 100)
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
}
