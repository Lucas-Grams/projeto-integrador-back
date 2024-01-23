
CREATE TABLE IF NOT EXISTS public.empresa (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     razao_social VARCHAR(100) NOT NULL,
     nome_fantasia VARCHAR(100) NOT NULL,
     cnpj VARCHAR(18) UNIQUE NOT NULL,
     id_endereco BIGINT REFERENCES endereco(id) NOT NULL,
     ativo BOOLEAN,
     uuid UUID DEFAULT uuid_generate_v4() NOT NULL,
     data_cadastro DATE,
     ultima_atualizacao DATE
);

CREATE TABLE IF NOT EXISTS public.empresa_usuario (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     id_empresa BIGINT REFERENCES empresa(id) NOT NULL,
     id_usuario BIGINT REFERENCES usuario(id) NOT NULL,
     ativo BOOLEAN,
     id_permissao BIGINT REFERENCES permissao(id) NOT NULL
);
