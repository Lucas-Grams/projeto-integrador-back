package br.com.pnipapi.repository;

import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.UnidadeUsuario;
import br.com.pnipapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnidadeUsuarioRepository extends JpaRepository<UnidadeUsuario, Long> {
    @Query(value = """
    SELECT p.*
    FROM permissao p
             JOIN unidade_usuario uu ON p.id = uu.id_permissao
             JOIN unidade un ON uu.id_unidade = un.id
    WHERE uu.id_usuario = :id AND un.id = :id_unidade;
""", nativeQuery = true)
    List<Permissao> findPermissoesByUsuarioId(@Param("id") Long id, @Param("id_unidade") Long id_unidade);

    List<UnidadeUsuario> findAllByUnidadeId(@Param("id") Long id);

//    @Query(value = """
//SELECT  u.* ,p.*
//FROM permissao p
//         JOIN unidade_usuario uu ON p.id = uu.id_permissao
//         JOIN unidade un ON uu.id_unidade = un.id
//        JOIN usuario u ON uu.id_usuario = u.id
//WHERE un.id = ?;
//""", nativeQuery = true)
//    List<UnidadeUsuario> findUsuariosUnidadePermissao(@Param("id") Long id);

List<UnidadeUsuario> findAllByUsuario_Id(@Param("id") Long id);
}
