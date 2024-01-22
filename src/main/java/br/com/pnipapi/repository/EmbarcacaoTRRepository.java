package br.com.pnipapi.repository;

import br.com.pnipapi.model.EmbarcacaoTR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmbarcacaoTRRepository extends JpaRepository<EmbarcacaoTR, Long>  {

    @Query(nativeQuery = true, value = """
        SELECT * FROM public.embarcacao_tr WHERE id_embarcacao = :idEmbarcacao
    """)
    Optional<EmbarcacaoTR> findByIdEmbarcacao(Long idEmbarcacao);

    @Query(nativeQuery = true, value = """
        SELECT * FROM public.embarcacao_tr WHERE uuid = :uuid
    """)
    Optional<EmbarcacaoTR> findByUuid(UUID uuid);

    @Query(nativeQuery = true, value = """ 
        SELECT _this.id, cast(_this.uuid as text), _this.ativo, _this.data_criacao,
            e.num_rgp, e.nome, f.codigo, _this.id_embarcacao
        FROM public.embarcacao_tr _this, public.embarcacao e, public.embarcacao_frota ef, public.frota f
        WHERE e.id = _this.id_embarcacao
        AND ef.id_embarcacao = e.id
        AND f.id = ef.id_frota
        AND _this.id_usuario = :idUsuario
        AND _this.ativo = true
        ORDER BY _this.id DESC
    """)
    List<Object[]> minhasEmbarcacoes(Long idUsuario);

}
