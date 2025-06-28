CREATE TABLE public.saida
(
    codigo bigserial NOT NULL,
    codigo_funcionario bigint,
    codigo_veiculo bigint,
    data_hora_saida timestamp without time zone,
    data_hora_retorno timestamp without time zone,
    km_saida double precision,
    km_retorno double precision,
    destino text,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo),
    FOREIGN KEY (codigo_funcionario) REFERENCES funcionario(codigo),
    FOREIGN KEY (codigo_veiculo) REFERENCES veiculo(codigo)
);