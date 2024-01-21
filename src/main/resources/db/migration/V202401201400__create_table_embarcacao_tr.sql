DROP TABLE IF EXISTS public.embarcacao_solicitar_habilitacao;

CREATE TABLE IF NOT EXISTS public.embarcacao_tr (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    id_embarcacao BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,
    nome_proprietario VARCHAR(255) NOT NULL,
    cpf_proprietario VARCHAR(255) NOT NULL,
    email_proprietario VARCHAR(255) NOT NULL,
    id_arquivo_declaracao_proprietario BIGINT NOT NULL,
    mercado_atuacao VARCHAR(255) NOT NULL,
    tempo_medio_pesca INT NOT NULL,
    tipo_conservacao TEXT NOT NULL,
    capacidade_total INT NOT NULL,
    capacidade_pescado INT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_embarcacao) REFERENCES public.embarcacao(id),
    FOREIGN KEY (id_usuario) REFERENCES public.usuario(id),
    FOREIGN KEY (id_arquivo_declaracao_proprietario) REFERENCES public.arquivo(id)
);

DELETE FROM public.status_solicitar_habilitacao;
DELETE FROM public.arquivo_solicitar_habilitacao;
DELETE FROM public.arquivo;
DELETE FROM public.solicitar_habilitacao;
