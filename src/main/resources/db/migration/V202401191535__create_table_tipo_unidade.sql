CREATE TABLE tipo_unidade (
                                 id BIGSERIAL PRIMARY KEY,
                                 nome varchar(100),
                                 tipo varchar(10)
);

INSERT INTO public.tipo_unidade (nome, tipo) VALUES ('Unidade Central (UC)', 'UC');
INSERT INTO public.tipo_unidade (nome, tipo) VALUES ('Programa/Seção (PS)', 'PS');
INSERT INTO public.tipo_unidade (nome, tipo) VALUES ('Supeorvisão Regional (SR)', 'SR');
INSERT INTO public.tipo_unidade (nome, tipo) VALUES ('Inspetoria Vetrinária Local (IVZ)', 'IVZ');
INSERT INTO public.tipo_unidade (nome, tipo) VALUES ('Ministério da Pesca e Aquicultura (MPA)', 'MPA');
INSERT INTO public.tipo_unidade (nome, tipo) VALUES ('Secretaria Nacional (SN)', 'SN');
INSERT INTO public.tipo_unidade (nome, tipo) VALUES ('Departamento (DP)', 'DP');
INSERT INTO public.tipo_unidade (nome, tipo) VALUES ('Superintendencia Federal da Pesca (SFP)', 'SFP')

ALTER TABLE unidade_usuario
ALTER COLUMN id TYPE bigint;

ALTER TABLE unidade_usuario
DROP CONSTRAINT unidade_usuario_pkey;

ALTER TABLE unidade_usuario
    ADD PRIMARY KEY (id_unidade, id_usuario, id_permissao);


INSERT INTO permissao (descricao) VALUES ('so');
INSERT INTO permissao (descricao) VALUES ('representante');
