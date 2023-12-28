package br.com.pnipapi.repository;

import br.com.pnipapi.model.ArquivoSolicitarHabilitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoSolicitarHabilitacaoRepository extends JpaRepository<ArquivoSolicitarHabilitacao, Long>  {
}
