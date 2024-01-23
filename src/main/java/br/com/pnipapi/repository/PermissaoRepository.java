package br.com.pnipapi.repository;
import br.com.pnipapi.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Set;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    public List<Permissao> findAllByDescricaoIn(List<String> descricoes);

    @Query(value="""
        SELECT p.* FROM public.permissao p
        WHERE p.descricao = :descricao """, nativeQuery = true)
    Permissao findPermissaoByName(@Param("descricao") String descricao);

    @Modifying
    @Query(value= """
    INSERT INTO usuario_permissao (id_usuario, id_permissao) VALUES (:idUsuario, :idPermissao)
""", nativeQuery = true)
    void savePermissao(@Param("idUsuario") Long idUsuario, @Param("idPermissao") Set<Long> idPermissao);
}
