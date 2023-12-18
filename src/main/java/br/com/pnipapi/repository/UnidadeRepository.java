package br.com.pnipapi.repository;

import br.com.pnipapi.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    @Query(value = " SELECT * FROM unidade u " +
        "   WHERE u.tipo = :tipo AND ativo = true", nativeQuery = true)
    List<Unidade> getUnidadeByTipo(@Param("tipo") String tipo);

    List<Unidade> findByTipo(String tipo);

    @Query(value = "SELECT * FROM unidade u " +
        "  WHERE u.uuid = CAST(:uuid AS UUID)", nativeQuery = true)
    Unidade findByUuid(@Param("uuid") String uuid);


    @Query(value = "INSERT INTO unidade_usuario (id_unidade, id_usuario, ativo) " +
        "VALUES (:id_unidade, :id_usuario, true)", nativeQuery = true)
    Integer salvarRepresentante(@Param("id_unidade") int id_unidade, @Param("id_usuario") int id_usuario);

    @Query(value="UPDATE unidade_usuario SET ativo = false  WHERE id_unidade =:id_unidade", nativeQuery = true)
    Unidade updateRepresentante(@Param("id_unidade") int id_unidade);

    List<Unidade> findAllByAtivo(boolean ativo);
}
