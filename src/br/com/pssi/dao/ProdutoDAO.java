package br.com.pssi.dao;

import br.com.pssi.model.Produto;
import br.com.pssi.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void salvar(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (codigo_barras, nome, quantidade, preco_custo, preco_venda, categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getCodigoBarras());
            stmt.setString(2, produto.getNome());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setDouble(4, produto.getPrecoCusto());
            stmt.setDouble(5, produto.getPrecoVenda());
            stmt.setString(6, produto.getCategoria());
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        String sql = "SELECT * FROM produtos";
        List<Produto> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Produto(
                        rs.getInt("id"), rs.getString("codigo_barras"), rs.getString("nome"),
                        rs.getInt("quantidade"), rs.getDouble("preco_custo"),
                        rs.getDouble("preco_venda"), rs.getString("categoria")
                ));
            }
        }
        return lista;
    }

    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET codigo_barras=?, nome=?, quantidade=?, preco_custo=?, preco_venda=?, categoria=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getCodigoBarras());
            stmt.setString(2, produto.getNome());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setDouble(4, produto.getPrecoCusto());
            stmt.setDouble(5, produto.getPrecoVenda());
            stmt.setString(6, produto.getCategoria());
            stmt.setInt(7, produto.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}