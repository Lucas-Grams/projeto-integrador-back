CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';

SET TIMEZONE='Brazil/East';

CREATE TABLE IF NOT EXISTS public.endereco (
	id BIGSERIAL not null primary key,
	rua         varchar(70),
	cep         varchar(70),
	numero      varchar(10),
	complemento varchar(70),
	bairro      varchar(50),
	cidade      varchar(50),
	uf          varchar(50),
	latitude    varchar(50),
	longitude   varchar(50)
);

CREATE TABLE IF NOT EXISTS public.permissao (
	id BIGSERIAL not null primary key,
	descricao text
);

CREATE TABLE IF NOT EXISTS public.usuario (
	id BIGSERIAL not null primary key,
	cpf varchar(14) not null unique,
	nome  varchar(100) not null,
	email text not null unique,
	senha varchar(400) not null,
	data_cadastro date,
	ultimo_acesso date,
	id_endereco bigint,
	ativo boolean,
	foto  text,
	uuid  uuid default uuid_generate_v4() not null,
	ultima_atualizacao_cadastro date,
    FOREIGN KEY (id_endereco) references public.endereco(id)
);
CREATE INDEX ON public.usuario(email);





