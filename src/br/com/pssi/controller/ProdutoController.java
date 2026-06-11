package br.com.pssi.controller;

import br.com.pssi.dao.ProdutoDAO;
import br.com.pssi.model.Produto;
import java.sql.SQLException;
import java.util.List;

public class ProdutoController {
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public String cadastrarProduto(String codigo, String nome, String qtdStr, String custoStr, String vendaStr, String cat) {
        if (codigo.trim().isEmpty() || nome.trim().isEmpty() || qtdStr.isEmpty() || custoStr.isEmpty() || vendaStr.isEmpty()) {
            return "Erro: Todos os campos são obrigatórios.";
        }
        try {
            int qtd = Integer.parseInt(qtdStr);
            double custo = Double.parseDouble(custoStr.replace(",", "."));
            double venda = Double.parseDouble(vendaStr.replace(",", "."));

            if (qtd < 0 || custo < 0 || venda < 0) return "Erro: Valores não podem ser negativos.";

            produtoDAO.salvar(new Produto(0, codigo, nome, qtd, custo, venda, cat));
            return "Sucesso: Produto cadastrado com sucesso!";
        } catch (NumberFormatException e) {
            return "Erro: Formato numérico inválido nos campos de quantidade ou preço.";
        } catch (SQLException e) {
            return "Erro no Banco de Dados: " + e.getMessage();
        }
    }

    public List<Produto> listarProdutos() throws SQLException { return produtoDAO.listarTodos(); }

    public String deletarProduto(int id) {
        try {
            produtoDAO.excluir(id);
            return "Sucesso: Produto removido com sucesso!";
        } catch (SQLException e) {
            return "Erro ao excluir produto: " + e.getMessage();
        }
    }
}