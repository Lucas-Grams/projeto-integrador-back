CREATE TABLE IF NOT EXISTS public.embarcacao (
	id BIGSERIAL not null primary key,
	nome         varchar(100),
	num_marinha_tie         varchar(70),
    num_marinha varchar(70),
	num_rgp      varchar(50),
    sei_bphs varchar(50),
	uf          varchar(70),
    pais          varchar(70),
    ano_construcao integer,
    especie_alvo varchar(50),
    propulsao varchar(50),
    hp integer,
    combustivel varchar(50),
    comprimento varchar(50),
    material_casco varchar(50),
    canal_radio integer,
    equipamento_comunicacao varchar(50),
    metodo_pesca varchar(50),
    area_pesca varchar(50),
    zona_operacao varchar(50),
    subvencao_oleo_diesel boolean,
    entidade_colaboradora varchar(50),
    responsavel varchar(100),
    preps_ativo boolean,
    arqueacao_bruta integer,
    porto_origem varchar(70),
    porto_desemb varchar(70),
    embarcacao_arrendada boolean,
    empresa varchar(70),
    capac_porao integer,
    volume_tanque float,
    qtd_tripulacao integer,
    num_covos integer,
    petrecho varchar(70),
    codigo_in varchar(70)
);

CREATE TABLE IF NOT EXISTS public.frota (
	id BIGSERIAL not null primary key,
	codigo varchar(50),
	descricao text
);

CREATE TABLE IF NOT EXISTS public.embarcacao_frota (
    id BIGSERIAL not null primary key,
    id_embarcacao integer,
    id_frota integer,
    FOREIGN KEY (id_embarcacao) references public.embarcacao(id),
    FOREIGN KEY (id_frota) references public.frota(id)
);





