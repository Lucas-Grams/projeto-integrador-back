CREATE TABLE unidade_usuario (
                                 id SERIAL PRIMARY KEY,
                                 id_unidade BIGINT,
                                 id_usuario BIGINT,
                                 ativo BOOLEAN,
                                 id_permissao BIGINT,
                                 FOREIGN KEY (id_unidade) REFERENCES unidade(id),
                                 FOREIGN KEY (id_usuario) REFERENCES usuario(id),
                                 FOREIGN KEY (id_permissao) REFERENCES permissao(id)
);
