package br.com.pnipapi.repository;

import br.com.pnipapi.model.Embarcacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmbarcacaoRepository extends JpaRepository<Embarcacao, Long> {

    @Query(nativeQuery = true, value = """ 
        SELECT *  
        FROM public.embarcacao e
        WHERE ((e.nome ilike :filter%) OR (e.num_marinha_tie ilike :filter%) OR (e.num_marinha ilike :filter%) OR (e.num_rgp ilike :filter%)) 
         AND e.id not in (select id_embarcacao from public.embarcacao_tr et where et.ativo = true)
        LIMIT 10
        """)
    List<Embarcacao> findAllEmbarcacaoByRgpTieNome(String filter);

    @Query(nativeQuery = true, value = """ 
        SELECT f.codigo  
        FROM public.embarcacao e
        JOIN public.embarcacao_frota ef on ef.id_embarcacao = e.id
        JOIN public.frota f on f.id = ef.id_frota
        WHERE e.id = :idEmbarcacao
        """)
    String findFrotaByIdEmbarcacao(Long idEmbarcacao);

    //TODO - Feito para evitar cadastrar embarcações ja catalogadas manualmente
    @Query(nativeQuery = true, value = """ 
        SELECT e.id, f.codigo FROM public.embarcacao_tr_temp ett
        JOIN public.embarcacao e on e.id = ett.id_embarcacao
        JOIN public.embarcacao_frota ef on ef.id_embarcacao = e.id
        JOIN public.frota f on f.id = ef.id_frota
        WHERE ett.cpf = :cpf 
        """)
    List<Object[]> findEmbarcacoesByCpfTR(String cpf);

    @Query(nativeQuery = true, value = """ 
        SELECT tipo_certificacao 
        FROM public.certificado c
        WHERE c.id_embarcacao = :idEmbarcacao 
        """)
    String findCertificadoByIdEmbarcacao(Long idEmbarcacao);

}
