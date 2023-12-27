package br.com.pnipapi.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Data
@Table(name = "permissao", schema = "public")
@Entity
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false, length = 255)
    @Length(max = 255)
    private String descricao;

}
