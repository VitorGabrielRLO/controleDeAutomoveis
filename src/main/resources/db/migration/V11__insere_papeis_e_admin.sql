-- Inserindo os papéis (roles) básicos do sistema, caso ainda não existam.
-- O 'ON CONFLICT DO NOTHING' evita erro se eles já foram inseridos pelo script V10.
INSERT INTO papel(codigo, nome) VALUES (1, 'ROLE_ADMIN') ON CONFLICT (codigo) DO NOTHING;
INSERT INTO papel(codigo, nome) VALUES (2, 'ROLE_USUARIO') ON CONFLICT (codigo) DO NOTHING;

-- Inserindo o usuário administrador padrão
-- A senha é '12345', criptografada com Argon2.
-- O 'ON CONFLICT DO NOTHING' evita erro se o usuário 'admin' já existe.
INSERT INTO usuario(codigo, nome, email, nome_usuario, senha, data_nascimento, ativo) 
VALUES (1, 'Administrador', 'admin@sistema.com', 'admin', '{argon2}$argon2id$v=19$m=16384,t=2,p=1$TEhcmEx3SjZ5eFBpWm5sTg$L/QkS0f4sD4Gf3S0hTImr9eG4eXzz/3d3d2jcFHh/VM', '2000-01-01', true)
ON CONFLICT (codigo) DO NOTHING;

-- Associando o papel de ADMIN ao usuário 'admin'
-- O 'ON CONFLICT DO NOTHING' evita erro se a associação já existe.
INSERT INTO usuario_papel(codigo_usuario, codigo_papel) VALUES (1, 1) ON CONFLICT DO NOTHING;