package br.com.pnipapi.dto;

import br.com.pnipapi.model.Empresa;
import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaUsuarioDTO {
    private Long id;
    private Empresa empresa;
    private Usuario usuario;
    private List<Permissao> permissao;
    private boolean ativo;
}
