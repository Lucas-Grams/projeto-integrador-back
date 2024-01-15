package br.com.pnipapi.dto;

import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnidadeUsuarioDTO {



        private Long id;

        private Unidade unidade;

        private Usuario usuario;

        private Permissao[] permissao;

        private boolean ativo;



}
