package br.com.pnipapi.repository;

import br.com.pnipapi.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    public List<Permissao> findAllByDescricaoIn(List<String> descricoes);
}
