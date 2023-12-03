CREATE TABLE IF NOT EXISTS public.unidade (
	id BIGSERIAL PRIMARY KEY,
	nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    id_unidade_gerenciadora BIGINT,
    id_endereco BIGINT,
    ativo BOOLEAN DEFAULT TRUE,
    uuid uuid DEFAULT uuid_generate_v4() NOT NULL,
    data_cadastro DATE,
    ultima_atualizacao DATE,
    FOREIGN KEY (id_unidade_gerenciadora) REFERENCES public.unidade(id),
    FOREIGN KEY (id_endereco) REFERENCES public.endereco(id)
);
