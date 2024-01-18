package br.com.pnipapi.repository;

import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.UnidadeUsuario;
import br.com.pnipapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnidadeUsuarioRepository extends JpaRepository<UnidadeUsuario, Long> {

    List<UnidadeUsuario> findAllByUnidadeId(@Param("id") Long id);

List<UnidadeUsuario> findAllByUsuario_Id(@Param("id") Long id);


@Query(value= """
    SELECT id FROM unidade_usuario WHERE id_usuario = :id_usuario AND id_permissao = :id_permissao AND id_unidade = :id_unidade
    """, nativeQuery = true)
    Long findIdByUsuarioIdPermissaoID(@Param("id_usuario")Long id_usuario, @Param("id_permissao") Long id_permissao, @Param("id_unidade") Long id_unidade);

}
