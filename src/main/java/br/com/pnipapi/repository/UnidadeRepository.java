package br.com.pnipapi.repository;

import br.com.pnipapi.dto.UnidadeFormDTO;
import br.com.pnipapi.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
}
