package br.com.pnipapi.repository;

import br.com.pnipapi.model.TipoUnidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoUnidadeRepository extends JpaRepository<TipoUnidade, Long> {
}
