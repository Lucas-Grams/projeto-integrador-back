package br.com.pnipapi.repository;

import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    @Query(value = " SELECT * FROM unidade u " +
        "   WHERE u.tipo = :tipo ", nativeQuery = true)
    List<Unidade> getUnidadeByTipo(@Param("tipo") String tipo);

    @Query(value = "SELECT * FROM unidade u " +
        "  WHERE u.uuid = CAST(:uuid AS UUID)", nativeQuery = true)
    Unidade findByUuid(@Param("uuid") String uuid);

    @Query(value = "INSERT INTO unidade_usuario (id_unidade, id_usuario, ativo) " +
        "VALUES (:id_unidade, :id_usuario, true)", nativeQuery = true)
    void salvarRepresentante(@Param("id_unidade") int id_unidade, @Param("id_usuario") int id_usuario);

}
