CREATE TABLE public.arquivo (
    id             BIGSERIAL PRIMARY KEY,
    nome           TEXT,
    caminho        TEXT,
    origem_arquivo TEXT,
    tamanho BIGINT,
    tipo varchar (40)
);

CREATE TABLE public.arquivo_solicitar_habilitacao (
    id          BIGSERIAL PRIMARY KEY,
    id_arquivo BIGINT,
    id_solicitacao BIGINT,
    id_embarcacao BIGINT, -- pode ou não existir
    FOREIGN KEY (id_solicitacao) REFERENCES public.solicitar_habilitacao(id),
    FOREIGN KEY (id_arquivo) REFERENCES public.arquivo(id)
);

