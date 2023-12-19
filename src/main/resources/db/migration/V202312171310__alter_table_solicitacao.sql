ALTER TABLE public.solicitar_habilitacao ADD status varchar(30);
ALTER TABLE public.solicitar_habilitacao ADD solicitante varchar(100);
ALTER TABLE public.solicitar_habilitacao ADD protocolo varchar(100);
ALTER TABLE public.solicitar_habilitacao ADD observacao varchar(100);

CREATE TABLE IF NOT EXISTS public.status (
    id BIGSERIAL PRIMARY KEY,
    descricao varchar(30)
);

INSERT INTO public.status(id, descricao) VALUES (DEFAULT, 'EM_ANALISE');
INSERT INTO public.status(id, descricao) VALUES (DEFAULT, 'DEFERIDA');
INSERT INTO public.status(id, descricao) VALUES (DEFAULT, 'INDEFERIDA');

CREATE TABLE IF NOT EXISTS public.status_solicitar_habilitacao (
    id BIGSERIAL PRIMARY KEY,
    id_solicitacao BIGINT,
    id_status BIGINT,
    FOREIGN KEY (id_solicitacao) REFERENCES public.solicitar_habilitacao(id),
    FOREIGN KEY (id_status) REFERENCES public.status(id)
);

CREATE TABLE IF NOT EXISTS public.embarcacao_solicitar_habilitacao (
   id BIGSERIAL PRIMARY KEY,
   id_solicitacao BIGINT,
   id_embarcacao BIGINT,
   aprovado boolean,
   FOREIGN KEY (id_solicitacao) REFERENCES public.solicitar_habilitacao(id),
   FOREIGN KEY (id_embarcacao) REFERENCES public.embarcacao(id)
);

insert into processos.tipo_processo (id, descricao, ativo) values (DEFAULT, 'HABILITACAO_TR', true);
