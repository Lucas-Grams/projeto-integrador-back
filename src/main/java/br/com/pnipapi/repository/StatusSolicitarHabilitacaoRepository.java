package br.com.pnipapi.repository;

import br.com.pnipapi.model.StatusSolicitarHabilitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusSolicitarHabilitacaoRepository extends JpaRepository<StatusSolicitarHabilitacao, Long>  {

}
