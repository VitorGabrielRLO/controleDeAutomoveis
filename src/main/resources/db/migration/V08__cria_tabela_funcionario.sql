CREATE TABLE public.funcionario
(
    codigo bigserial NOT NULL,
    nome text,
    matricula text,
    cpf text,
    data_nascimento date,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);