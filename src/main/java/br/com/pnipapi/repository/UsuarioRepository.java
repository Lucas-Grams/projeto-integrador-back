package br.com.pnipapi.repository;

import br.com.pnipapi.model.Permissao;
import br.com.pnipapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
    @Query(value="""
        SELECT u.* FROM usuario u
        WHERE u.cpf = :cpf """, nativeQuery = true)
    Optional<Usuario> findUsuarioByCpf(@Param("cpf") String cpf);

    Optional<Usuario> findById(@Param("id") Long id);

    Optional<Usuario> findAllByUuid(@Param("uuid") UUID uuid);


    @Query(value = """
    SELECT p.*
    FROM permissao p
             JOIN unidade_usuario uu ON p.id = uu.id_permissao
             JOIN unidade un ON uu.id_unidade = un.id
    WHERE uu.id_usuario = :id AND un.id = :id_unidade;
""", nativeQuery = true)
    List<Permissao> findPermissoesByUsuarioId(@Param("id") Long id, @Param("id_unidade") Long id_unidade);

    @Query(value = """
    SELECT DISTINCT u.id, u.* FROM usuario u 
    JOIN unidade_usuario  uu ON u.id = uu.id_usuario  
""", nativeQuery = true)
    List<Usuario> findUsuariosDip();

    @Modifying
    @Query(value="""
   INSERT INTO usuario_permissao (id_usuario, id_permissao) VALUES(:id_usuario, :id_permissao)
        """, nativeQuery = true)
    void savePermissao(@Param("id_usuario") Long id_usuario, @Param("id_permissao") Long id_permissao);

    @Query(value = """
    SELECT COUNT(*) FROM usuario_permissao up
    WHERE up.id_usuario = :id_usuario AND up.id_permissao = :id_permissao
""", nativeQuery = true)
    int countPermissaoByUsuario(@Param("id_usuario") Long idUsuario, @Param("id_permissao") Long idPermissao);



}
