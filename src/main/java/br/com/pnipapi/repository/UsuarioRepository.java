package br.com.pnipapi.repository;

import br.com.pnipapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {

    @Query(value = "SELECT u.*, p.* FROM usuario u " +
        " JOIN unidade_usuario uus ON uus.id_usuario = u.id " +
        " JOIN unidade un ON un.id = uus.id_unidade " +
        " JOIN public.permissao p on uus.id_permissao = p.id " +
        " WHERE un.uuid = cast(:uuid as uuid)", nativeQuery = true)
    List<Usuario> findUsuariosUnidade(@Param("uuid") String uuid);

    @Query(value="SELECT u.*, p.* FROM usuario u " +
        " JOIN unidade_usuario uus ON uus.id_usuario = u.id " +
        " JOIN unidade un ON un.id = uus.id_unidade " +
        " JOIN public.permissao p on uus.id_permissao = p.id " +
        " WHERE un.id = :id_unidade", nativeQuery = true)
    List<Usuario> findRepresentantes(@Param("id_unidade") long id_unidade);

}
