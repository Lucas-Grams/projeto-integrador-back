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
    List<Unidade> findUnidadesByTipo(@Param("tipo") String tipo);


    @Query(value = "SELECT * FROM unidade u " +
        "  WHERE u.uuid = CAST(:uuid AS UUID)", nativeQuery = true)
    Unidade findUnidadeByUuid(@Param("uuid") String uuid);


    @Modifying
    @Query(value="UPDATE unidade_usuario SET ativo =:ativo  WHERE id_unidade =:idUnidade", nativeQuery = true)
    void updateUsuariosByIdUnidade(long idUnidade, boolean  ativo);


    Optional<Unidade> findAllByUuid(@Param("uuid") UUID uuid);
}
