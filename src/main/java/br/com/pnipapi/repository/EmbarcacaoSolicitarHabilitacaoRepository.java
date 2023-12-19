package br.com.pnipapi.repository;

import br.com.pnipapi.model.EmbarcacaoSolicitarHabilitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmbarcacaoSolicitarHabilitacaoRepository extends JpaRepository<EmbarcacaoSolicitarHabilitacao, Long>  {

}
