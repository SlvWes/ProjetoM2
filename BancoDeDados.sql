-- 1. Criação do Banco de Dados (com suporte a acentuação PT-BR)
CREATE DATABASE IF NOT EXISTS pssi_storage
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE pssi_storage;

-- 2. Tabela de Usuários (Controle de Acesso Administrativo e Operacional)
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(64) NOT NULL, -- Tamanho fixo de 64 caracteres para armazenar o hash SHA-256
    perfil VARCHAR(20) NOT NULL, -- Define a hierarquia: 'ADMINISTRADOR' ou 'OPERADOR'
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 3. Tabela de Produtos (Entidade Principal do CRUD de Gestão de Estoque)
CREATE TABLE IF NOT EXISTS produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_barras VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL DEFAULT 0,
    preco_custo DECIMAL(10,2) NOT NULL,
    preco_venda DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(50),
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

DELETE FROM usuarios;
DELETE FROM produtos;

INSERT INTO usuarios (nome, login, senha, perfil) 
VALUES ('Administrador', 'admin', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'ADMINISTRADOR');

SELECT 	* FROM pssi_storage.produtos