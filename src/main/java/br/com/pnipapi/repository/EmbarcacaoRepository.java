package br.com.pnipapi.repository;

import br.com.pnipapi.model.Embarcacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmbarcacaoRepository extends JpaRepository<Embarcacao, Long> {

    @Query(nativeQuery = true, value = """ 
        SELECT *  
        FROM public.embarcacao e
        WHERE (e.nome like :filter) or (e.num_marinha_tie like :filter) or (e.num_marinha like :filter) or (e.num_rgp like :filter) LIMIT 10
        """)
    List<Embarcacao> findAllEmbarcacaoByRgpTieNome(String filter);

}
