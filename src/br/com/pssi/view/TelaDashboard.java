package br.com.pssi.view;

import br.com.pssi.controller.ControleSessao;
import br.com.pssi.controller.ProdutoController;
import br.com.pssi.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class TelaDashboard extends JFrame {
    private final ProdutoController controller = new ProdutoController();
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;
    private JTextField txtCodigo, txtNome, txtQtd, txtCusto, txtVenda, txtCategoria;
    private int idProdutoSelecionado = 0;

    public TelaDashboard() {
        if (!ControleSessao.isAutenticado()) {
            JOptionPane.showMessageDialog(null, "Sessão inválida. Faça login novamente.");
            System.exit(0);
        }
        initUI();
        carregarTabela();
    }

    private void initUI() {
        setTitle("Secure Storage ERP - Console Corporativo");

        // Configuração do Ícone do Aplicativo
        configurarInconeApp();

        // Configuração para abrir automaticamente em Tela Cheia
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 768));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel painelMain = new JPanel(new BorderLayout());
        painelMain.setBackground(new Color(9, 9, 11));

        // 1. Barra Superior (Header do Sistema)
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setBackground(new Color(24, 24, 27));
        painelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblModulo = new JLabel("MÓDULO DE INVENTÁRIO & ALMOXARIFADO");
        lblModulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblModulo.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("Operador Atual: " + ControleSessao.getUsuarioLogado().getNome() + " [" + ControleSessao.getUsuarioLogado().getPerfil() + "]");
        lblUser.setForeground(new Color(9, 9, 99));
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        painelHeader.add(lblModulo, BorderLayout.WEST);
        painelHeader.add(lblUser, BorderLayout.EAST);
        painelMain.add(painelHeader, BorderLayout.NORTH);

        // 2. Formulário Lateral Esquerdo (Inserção de Dados)
        JPanel painelFormContainer = new JPanel(new BorderLayout());
        painelFormContainer.setBackground(new Color(24, 24, 27));
        painelFormContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelCampos = new JPanel(new GridLayout(6, 2, 10, 15));
        painelCampos.setBackground(new Color(24, 24, 27));

        txtCodigo = criarTextField(); txtNome = criarTextField(); txtQtd = criarTextField();
        txtCusto = criarTextField(); txtVenda = criarTextField(); txtCategoria = criarTextField();

        adicionarCampo(painelCampos, "Cód. de Barras:", txtCodigo);
        adicionarCampo(painelCampos, "Nome do Item:", txtNome);
        adicionarCampo(painelCampos, "Qtd em Estoque:", txtQtd);
        adicionarCampo(painelCampos, "Preço de Custo:", txtCusto);
        adicionarCampo(painelCampos, "Preço de Venda:", txtVenda);
        adicionarCampo(painelCampos, "Categoria:", txtCategoria);

        JPanel painelAcoes = new JPanel(new GridLayout(3, 1, 10, 10));
        painelAcoes.setBackground(new Color(24, 24, 27));
        painelAcoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnSalvar = new JButton("Registrar / Atualizar");
        btnSalvar.setBackground(new Color(9, 9, 99));
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> salvar());

        JButton btnDeletar = new JButton("Remover Selecionado");
        btnDeletar.setBackground(new Color(239, 68, 68));
        btnDeletar.setForeground(Color.BLACK);
        btnDeletar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDeletar.addActionListener(e -> deletar());

        JButton btnLimpar = new JButton("Limpar Campos / Novo");
        btnLimpar.setBackground(new Color(63, 63, 70));
        btnLimpar.setForeground(Color.BLACK);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpar.addActionListener(e -> limpar());

        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnDeletar);
        painelAcoes.add(btnLimpar);

        painelFormContainer.add(painelCampos, BorderLayout.CENTER);
        painelFormContainer.add(painelAcoes, BorderLayout.SOUTH);
        painelMain.add(painelFormContainer, BorderLayout.WEST);

        // 3. Área Central (Tabela de Dados com Cabeçalho Customizado)
        String[] colunas = {"ID único", "Código de Barras", "Descrição do Produto", "Qtd Física", "Preço Custo", "Preço Venda", "Categoria"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos = new JTable(tableModel);
        tabelaProdutos.setRowHeight(25);
        tabelaProdutos.setGridColor(new Color(39, 39, 42));
        tabelaProdutos.setBackground(new Color(32, 32, 35));
        tabelaProdutos.setForeground(Color.WHITE);

        tabelaProdutos.getTableHeader().setBackground(new Color(9, 9, 99));
        tabelaProdutos.getTableHeader().setForeground(Color.BLACK);
        tabelaProdutos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCamposParaEdicao();
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaProdutos);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        scroll.getViewport().setBackground(new Color(39, 39, 42));
        painelMain.add(scroll, BorderLayout.CENTER);

        add(painelMain);
    }

    private void configurarInconeApp() {
        try {
            // Busca o arquivo icone.png dentro do diretório de resources do projeto
            URL urlIcone = getClass().getResource("/resources/icone.png");
            if (urlIcone != null) {
                Image icone = Toolkit.getDefaultToolkit().getImage(urlIcone);
                setIconImage(icone);
            } else {
                System.out.println("Aviso: O arquivo /resources/icone.png não foi localizado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar o ícone: " + e.getMessage());
        }
    }

    private JTextField criarTextField() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(39, 39, 42));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(63, 63, 70)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return tf;
    }

    private void adicionarCampo(JPanel painel, String label, JTextField tf) {
        JLabel jl = new JLabel(label);
        jl.setForeground(new Color(0, 0, 177));
        jl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        painel.add(jl);
        painel.add(tf);
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Produto> produtos = controller.listarProdutos();
            for (Produto p : produtos) {
                tableModel.addRow(new Object[]{
                        p.getId(), p.getCodigoBarras(), p.getNome(),
                        p.getQuantidade(), p.getPrecoCusto(), p.getPrecoVenda(), p.getCategoria()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Falha de sincronização de dados: " + e.getMessage());
        }
    }

    private void preencherCamposParaEdicao() {
        int row = tabelaProdutos.getSelectedRow();
        if (row != -1) {
            idProdutoSelecionado = (int) tableModel.getValueAt(row, 0);
            txtCodigo.setText(tableModel.getValueAt(row, 1).toString());
            txtNome.setText(tableModel.getValueAt(row, 2).toString());
            txtQtd.setText(tableModel.getValueAt(row, 3).toString());
            txtCusto.setText(tableModel.getValueAt(row, 4).toString());
            txtVenda.setText(tableModel.getValueAt(row, 5).toString());
            txtCategoria.setText(tableModel.getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "");
        }
    }

    private void salvar() {
        String res = controller.cadastrarOuAtualizarProduto(
                String.valueOf(idProdutoSelecionado),
                txtCodigo.getText(), txtNome.getText(), txtQtd.getText(),
                txtCusto.getText(), txtVenda.getText(), txtCategoria.getText()
        );
        JOptionPane.showMessageDialog(this, res);
        carregarTabela();
        limpar();
    }

    private void deletar() {
        int row = tabelaProdutos.getSelectedRow();
        if(row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha da tabela.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);

        if (JOptionPane.showConfirmDialog(this, "Deseja remover o registro ID " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, controller.deletarProduto(id));
            carregarTabela();
            limpar();
        }
    }

    private void limpar() {
        idProdutoSelecionado = 0;
        txtCodigo.setText("");
        txtNome.setText("");
        txtQtd.setText("");
        txtCusto.setText("");
        txtVenda.setText("");
        txtCategoria.setText("");
        tabelaProdutos.clearSelection();
    }
}