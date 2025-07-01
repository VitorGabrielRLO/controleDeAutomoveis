-- Inserindo os papéis (roles) que o sistema usará
INSERT INTO papel(nome) VALUES ('ROLE_ADMIN');
INSERT INTO papel(nome) VALUES ('ROLE_USUARIO');

-- Criando um usuário administrador para testes (usuário: admin, senha: 12345)
-- A senha '12345' está criptografada com Argon2.
INSERT INTO usuario(nome, email, nome_usuario, senha, data_nascimento, ativo)
VALUES ('Administrador', 'admin@sistema.com', 'admin', '{argon2}$argon2id$v=19$m=16384,t=2,p=1$TEhcmEx3SjZ5eFBpWm5sTg$L/QkS0f4sD4Gf3S0hTImr9eG4eXzz/3d3d2jcFHh/VM', '2000-01-01', true);

-- Associando o papel de ADMIN ao usuário 'admin'
-- (Assumindo que o código do usuário 'admin' será 1 e do papel 'ROLE_ADMIN' será 1)
INSERT INTO usuario_papel(codigo_usuario, codigo_papel) VALUES (1, 1);