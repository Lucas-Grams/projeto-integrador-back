package br.com.pnipapi.repository;
import br.com.pnipapi.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Empresa findEmpresaByUuid(@Param("uuid") UUID uuid);

    long countEmpresasByCnpjAndAtivo(@Param("cnpj") String cnpj, @Param("ativo") boolean ativo);

    Optional<Empresa> findByCnpj(@Param("cnpj") String cnpj);
}
