-- Inserindo os papéis (roles) básicos do sistema
INSERT INTO papel(codigo, nome) VALUES (1, 'ROLE_ADMIN');
INSERT INTO papel(codigo, nome) VALUES (2, 'ROLE_USUARIO');

-- Inserindo o usuário administrador com senha em texto puro: '12345'
-- O prefixo {noop} é essencial para o Spring Security entender que não deve descriptografar.
INSERT INTO usuario(codigo, nome, email, nome_usuario, senha, data_nascimento, ativo) 
VALUES (1, 'Administrador', 'admin@sistema.com', 'admin', '{noop}12345', '2000-01-01', true);

-- Associando o papel de ADMIN ao usuário 'admin'
INSERT INTO usuario_papel(codigo_usuario, codigo_papel) VALUES (1, 1);