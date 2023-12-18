package br.com.pnipapi.repository;

import br.com.pnipapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {

    @Query(value = "SELECT u.* FROM usuario u " +
        "JOIN unidade_usuario uu ON uu.id_usuario = u.id " +
        "JOIN unidade un ON un.id = uu.id_unidade " +
        "AND un.uuid = CAST(:uuid AS UUID)", nativeQuery = true)
    List<Usuario> findRepresentantesUnidade(@Param("uuid") String uuid);

}
