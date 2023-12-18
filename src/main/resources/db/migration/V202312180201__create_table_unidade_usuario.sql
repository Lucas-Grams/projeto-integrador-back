CREATE TABLE unidade_usuario (
                                 id SERIAL PRIMARY KEY,
                                 id_unidade INT,
                                 id_usuario INT,
                                 ativo BOOLEAN,
                                 id_permissao INT,
                                 FOREIGN KEY (id_unidade) REFERENCES unidade(id),
                                 FOREIGN KEY (id_usuario) REFERENCES usuario(id),
                                 FOREIGN KEY (id_permissao) REFERENCES permissao(id)
);
