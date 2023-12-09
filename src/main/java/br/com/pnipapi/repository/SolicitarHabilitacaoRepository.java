package br.com.pnipapi.repository;

import br.com.pnipapi.model.SolicitarHabilitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitarHabilitacaoRepository extends JpaRepository<SolicitarHabilitacao, Long>  {
}
