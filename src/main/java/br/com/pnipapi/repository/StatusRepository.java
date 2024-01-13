package br.com.pnipapi.repository;

import br.com.pnipapi.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long>  {

    @Query(nativeQuery = true, value = """
        select * from public.status s where s.descricao ilike :descricao
    """)
    Optional<Status> findByDescricao(String descricao);

}
