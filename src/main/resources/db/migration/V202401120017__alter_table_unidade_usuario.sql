ALTER TABLE unidade_usuario
ALTER COLUMN id TYPE bigint;

ALTER TABLE unidade_usuario
DROP CONSTRAINT unidade_usuario_pkey;

ALTER TABLE unidade_usuario
    ADD PRIMARY KEY (id_unidade, id_usuario, id_permissao);

ALTER TABLE unidade_usuario
DROP COLUMN id;


