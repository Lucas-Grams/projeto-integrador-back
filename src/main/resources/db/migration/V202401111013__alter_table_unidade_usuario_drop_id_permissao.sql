ALTER TABLE unidade_usuario
DROP COLUMN id_permissao;

ALTER TABLE unidade_usuario
    ADD COLUMN permissao text[];
