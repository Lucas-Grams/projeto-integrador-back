package br.com.pnipapi.repository;

import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    @Query(value = " SELECT * FROM unidade u " +
        "   WHERE u.tipo = :tipo ", nativeQuery = true)
    List<Unidade> getUnidadeByTipo(@Param("tipo") String tipo);
}
