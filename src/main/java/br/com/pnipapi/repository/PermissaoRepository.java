package br.com.pnipapi.repository;

import br.com.pnipapi.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long>  {

    @Query(value="""
        SELECT p.* FROM public.permissao p
        WHERE p.descricao = :descricao """, nativeQuery = true)
    Permissao findPermissaoByName(@Param("descricao") String descricao);

}