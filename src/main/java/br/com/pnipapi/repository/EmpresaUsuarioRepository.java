package br.com.pnipapi.repository;
import br.com.pnipapi.model.EmpresaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmpresaUsuarioRepository extends JpaRepository<EmpresaUsuario, Long> {

    @Query(value= """
    SELECT id FROM empresa_usuario WHERE id_usuario = :idUsuario AND id_permissao = :idPermissao AND id_empresa = :idEmpresa
    """, nativeQuery = true)
    Long findIdByIdUsuarioIdPermissao(@Param("idUsuario")Long idUsuario, @Param("idPermissao") Long idPermissao, @Param("idEmpresa") Long idEmpresa);

    @Query(value= """
    SELECT * FROM empresa_usuario eu
    JOIN  empresa e ON eu.id_empresa = e.id AND e.uuid = CAST(:uuid AS UUID)
""", nativeQuery = true)
    List<EmpresaUsuario> findAllByUuidEmpresa(@Param("uuid") String uuid);

    List<EmpresaUsuario> findAllByEmpresaId(@Param("id") Long id);
}
