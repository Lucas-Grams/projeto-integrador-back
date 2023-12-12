-- Migration USUARIOS DEFAULT
insert into endereco (id, rua, cep, numero, complemento, bairro, cidade, uf, latitude, longitude)
values (DEFAULT, 'Av. Roraima', '97105-900', '1000', null, 'Camobi', 'Santa Maria', 'RS', null, null);

insert into usuario (id, cpf, nome, email, senha, data_cadastro, ultimo_acesso, id_endereco, ativo, foto, uuid, ultima_atualizacao_cadastro)
values (DEFAULT, '00000000000','ADMIN','admin@admin', '$2a$10$Pk7VXzAybPm.yQ65TFxUAu3P5/TVH0ByP2i412yCdWjQSyiVu51te', CURRENT_DATE, null, 1, true,
        null, '920ac960-6389-4f93-8f36-c86c2c9e7098', null);

insert into usuario (id, cpf, nome, email, senha, data_cadastro, ultimo_acesso, id_endereco, ativo, foto, uuid, ultima_atualizacao_cadastro)
values (DEFAULT, '00000000001', 'Usuario TR' ,'tr@tr', '$2a$10$Pk7VXzAybPm.yQ65TFxUAu3P5/TVH0ByP2i412yCdWjQSyiVu51te', CURRENT_DATE, null, 1, true,
        null, '9428d52c-2f5b-436b-9f89-1e29aa1b2346', null);

insert into usuario (id, cpf, nome, email, senha, data_cadastro, ultimo_acesso, id_endereco, ativo, foto, uuid, ultima_atualizacao_cadastro)
values (DEFAULT, '00000000003', 'Usuario DIP' ,'dip@dip', '$2a$10$Pk7VXzAybPm.yQ65TFxUAu3P5/TVH0ByP2i412yCdWjQSyiVu51te', CURRENT_DATE, null, 1,
        true, null, '7d473de7-361e-498c-818d-e93067542aeb', null);

insert into unidade (id, nome, tipo, id_unidade_gerenciadora, id_endereco, ativo, uuid, data_cadastro, ultima_atualizacao)
values (DEFAULT, 'Ministério da Pesca e Aquicultura (MPA)', 'UC', null, 1, true,'87bd05fb-b089-47a7-a898-40014d0029b0', CURRENT_DATE, null);

CREATE TABLE IF NOT EXISTS public.solicitar_habilitacao (
    id BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT,
    uuid_solicitacao uuid,
    data_solicitacao DATE,
    metadado jsonb,
    FOREIGN KEY (id_usuario) REFERENCES public.usuario(id)
);
