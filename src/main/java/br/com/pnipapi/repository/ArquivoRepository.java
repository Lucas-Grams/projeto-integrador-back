package br.com.pnipapi.repository;

import br.com.pnipapi.model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Long>  {

    @Query(nativeQuery = true, value = """
        SELECT * FROM public.arquivo a
        JOIN public.arquivo_solicitar_habilitacao ash on ash.id_arquivo = a.id
        WHERE ash.id_solicitacao = :idSolicitacao and a.nome = :nome
    """)
    Optional<Arquivo> findByIdSolicitacaoNome(Long idSolicitacao, String nome);

    @Query(nativeQuery = true, value = """
        SELECT * FROM public.arquivo a
        JOIN public.arquivo_solicitar_habilitacao ash on ash.id_arquivo = a.id
        WHERE ash.id_solicitacao = :idSolicitacao and ash.id_embarcacao = :idEmbarcacao
    """)
    Optional<Arquivo> findByIdSolicitacaoIdEmbarcacao(Long idSolicitacao, Long idEmbarcacao);

}
