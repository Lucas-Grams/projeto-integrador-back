package br.com.pnipapi.service.storage;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface StorageService {
    /**
     * Persiste o objeto no caminho {@code p}, com conteudo {@code bytes}
     * @param p Caminho para guardar
     * @param bytes conteudo do arquivo
     */
    void save(Path p, final byte[] bytes);

    /**
     * Lê objeto no caminho {@code p} como um {@link StorageObject}.
     *
     * @param p caminho para o objeto
     * @return Optitonal de StorageObject, contem um valor se existe um objet
     * o com o caminho {@code p}
     */
    Optional<StorageObject> read(Path p);

    /**
     * Lê tudo no caminho {@code p} de maneira recursiva, retornando uma lista
     * de objetos
     *
     * @param p caminho do diretorio onde os objetos
     * @return lista de objetos que tem o caminho como {@code p} como prefixo
     */
    List<StorageObject> readAll(Path p);

    /**
     * Apaga objeto  no caminho {@code p}.
     *
     * @param p Caminho do arquivo
     * @return retorna true se o arquivo foi apagado, false se não existe
     * @throws IllegalArgumentException se foi chamado com o caminho para um
     * diretório e ele não estiver vazio
     */
    boolean delete(Path p);

    /**
     * Apaga tudo em caminho {@code p} recursivamente
     *
     * @param p caminho onde a remoção recursiva irá iniciar.
     * @return o numero de objetos removidos
     */
    long deleteAllIn(Path p);
}
