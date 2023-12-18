insert into public.permissao (id, descricao) values (DEFAULT, 'admin');
insert into public.permissao (id, descricao) values (DEFAULT, 'tr');
insert into public.permissao (id, descricao) values (DEFAULT, 'mpa');

CREATE TABLE IF NOT EXISTS public.usuario_permissao (
    id BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT,
    id_permissao BIGINT,
    FOREIGN KEY (id_usuario) REFERENCES public.usuario(id),
    FOREIGN KEY (id_permissao) REFERENCES public.permissao(id)
);

insert into usuario_permissao (id, id_usuario, id_permissao) values (DEFAULT, 1, 1);
insert into usuario_permissao (id, id_usuario, id_permissao) values (DEFAULT, 2, 2);
insert into usuario_permissao (id, id_usuario, id_permissao) values (DEFAULT, 3, 3);
