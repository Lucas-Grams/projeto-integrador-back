package br.com.pnipapi.repository;

import br.com.pnipapi.model.Embarcacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbarcacaoRepository extends JpaRepository<Embarcacao, Long> {
}
