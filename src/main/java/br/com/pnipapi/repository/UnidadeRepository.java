package br.com.pnipapi.repository;

import br.com.pnipapi.model.Unidade;
import br.com.pnipapi.model.Usuario;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    @Query(value = " SELECT * FROM unidade u " +
        "   WHERE u.tipo = :tipo AND ativo = true", nativeQuery = true)
    List<Unidade> getUnidadeByTipo(@Param("tipo") String tipo);

    List<Unidade> findByTipo(String tipo);

    @Query(value = "SELECT * FROM unidade u " +
        "  WHERE u.uuid = CAST(:uuid AS UUID)", nativeQuery = true)
    Unidade findByUuid(@Param("uuid") String uuid);
    @Transactional
    @Modifying
    @Query(value = "UPDATE unidade_usuario SET ativo = true WHERE id_unidade =:id_unidade AND id_usuario=:id_usuario", nativeQuery = true)
    void salvarRepresentante(@Param("id_unidade") int id_unidade, @Param("id_usuario") int id_usuario);

    @Query(value="SELECT u.*, p.* FROM usuario u " +
        " JOIN unidade_usuario uus ON uus.id_usuario = u.id " +
        " JOIN unidade un ON un.id = uus.id_unidade " +
        " JOIN public.permissao p on uus.id_permissao = p.id " +
        " WHERE un.id = :id_unidade, nativeQuery = true)", nativeQuery = true)
    List<Usuario> findUsuarios(@Param("id_unidade") long id_unidade);

    @Modifying
    @Query(value="UPDATE unidade_usuario SET ativo =:ativo  WHERE id_unidade =:id_unidade", nativeQuery = true)
    void updateRepresentante(long id_unidade, boolean  ativo);

    @Query(value = """
    SELECT uuid FROM unidade WHERE uuid = cast(:uuid as uuid)
    """, nativeQuery = true)
    Long findIdByUuid(@Param("uuid") String uuid);

    List<Unidade> findAllByAtivo(boolean ativo);

    Optional<Unidade> findAllByUuid(@Param("uuid") UUID uuid);
}
