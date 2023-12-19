package br.com.pnipapi.repository;

import br.com.pnipapi.model.SolicitarHabilitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolicitarHabilitacaoRepository extends JpaRepository<SolicitarHabilitacao, Long>  {

    @Query(nativeQuery = true, value = """ 
        SELECT id, id_usuario, cast(uuid_solicitacao as text), data_solicitacao FROM public.solicitar_habilitacao WHERE id_usuario = :idUsuario ORDER BY id DESC
    """)
    List<Object[]> findAllSolicitacoesByIdUsuario(Long idUsuario);

    @Query(nativeQuery = true, value = """ 
        SELECT * FROM public.solicitar_habilitacao WHERE id_usuario = :idUsuario AND uuid_solicitacao = :uuid
    """)
    SolicitarHabilitacao findSolicitacaoByIdUsuarioAndUid(Long idUsuario, UUID uuid);

    @Query(nativeQuery = true, value = """
        select * from public.solicitar_habilitacao sh where sh.uuid_solicitacao = :uuid
    """)
    Optional<SolicitarHabilitacao> findByUuid(UUID uuid);

    @Query(nativeQuery = true, value = """
        select sh.* from public.solicitar_habilitacao sh 
        where sh.status = :status
    """)
    List<SolicitarHabilitacao> findSolicitacoesByStatus(String status);

}
