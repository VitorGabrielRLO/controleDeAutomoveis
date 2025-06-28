CREATE TABLE public.veiculo
(
    codigo bigserial NOT NULL,
    placa text,
    modelo text,
    ano integer,
    quilometragem double precision,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);