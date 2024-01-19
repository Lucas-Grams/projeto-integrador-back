package br.com.pnipapi.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
@Data
@Table(name = "empresa", schema = "public")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String razaoSocial;

    @Column(nullable = false)
    private String nomeFantasia;

    @Column(nullable = false)
    private String cnpj;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(nullable = false, name = "id_endereco")
    private Endereco endereco;

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
        name="empresa_usuario",
        joinColumns = @JoinColumn(name = "id_empresa"),
        inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<Usuario> usuarios;
}
