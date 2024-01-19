package br.com.pnipapi.repository;
import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.UnidadeUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UnidadeUsuarioRepository extends JpaRepository<UnidadeUsuario, Long> {

    @Query(value = """
    SELECT p.*
    FROM permissao p
             JOIN unidade_usuario uu ON p.id = uu.id_permissao
             JOIN unidade un ON uu.id_unidade = un.id
    WHERE uu.id_usuario = :idUsuario AND un.id = :idUnidade;
    """, nativeQuery = true)
    List<Permissao> findPermissoesByUsuarioId(@Param("idUsuario") Long idUsuario, @Param("idUnidade") Long idUnidade);

    List<UnidadeUsuario> findAllByUnidadeId(@Param("id") Long id);

    List<UnidadeUsuario> findAllByUsuario_Id(@Param("id") Long id);

    @Query(value= """
        SELECT id FROM unidade_usuario WHERE id_usuario = :idUsuario AND id_permissao = :idPermissao AND id_unidade = :idUnidade
    """, nativeQuery = true)
    Long findIdByUsuarioIdPermissaoID(@Param("idUsuario")Long idUsuario, @Param("idPermissao") Long idPermissao, @Param("idUnidade") Long idUnidade);
}
