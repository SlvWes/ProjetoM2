package br.com.pssi.view;

import br.com.pssi.dao.UsuarioDAO;
import br.com.pssi.model.Usuario;
import br.com.pssi.controller.ControleSessao;
import br.com.pssi.util.SecurityUtil;

import javax.swing.*;
import java.awt.*;
import java.net.URL; // IMPORTAÇÃO ADICIONADA PARA O ÍCONE

public class TelaLogin extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public TelaLogin() {
        initUI();
    }

    private void initUI() {
        setTitle("Secure Storage - Autenticação");
        setSize(450, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- BLOCO DO ÍCONE ADICIONADO AQUI ---
        try {
            URL urlIcone = getClass().getResource("/resources/icone.png");
            if (urlIcone != null) {
                setIconImage(new ImageIcon(urlIcone).getImage());
            } else {
                System.out.println("Aviso: Arquivo icone.png não encontrado em src/resources/");
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar o ícone: " + e.getMessage());
        }
        // --------------------------------------

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(new Color(24, 24, 27));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("SECURE STORAGE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(9, 9, 99));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        painelPrincipal.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        JLabel lblUser = new JLabel("Usuário:"); lblUser.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 1; painelPrincipal.add(lblUser, gbc);

        txtLogin = new JTextField(18);
        txtLogin.setBackground(new Color(39, 39, 42)); txtLogin.setForeground(Color.WHITE);
        txtLogin.setCaretColor(Color.WHITE);
        gbc.gridx = 1; painelPrincipal.add(txtLogin, gbc);

        JLabel lblSenha = new JLabel("Senha:"); lblSenha.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2; painelPrincipal.add(lblSenha, gbc);

        txtSenha = new JPasswordField(18);
        txtSenha.setBackground(new Color(39, 39, 42)); txtSenha.setForeground(Color.WHITE);
        txtSenha.setCaretColor(Color.WHITE);
        gbc.gridx = 1; painelPrincipal.add(txtSenha, gbc);

        btnEntrar = new JButton("Acessar Console");
        btnEntrar.setBackground(new Color(9, 9, 99));
        btnEntrar.setForeground(new Color(37, 99, 235)); // Texto do botão alterado para AZUL
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;

        btnEntrar.addActionListener(e -> logar());
        painelPrincipal.add(btnEntrar, gbc);

        add(painelPrincipal);
    }

    private void logar() {
        String login = txtLogin.getText();
        String senhaPura = new String(txtSenha.getPassword());

        if(login.isEmpty() || senhaPura.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos lógicos.");
            return;
        }

        String senhaCripto = SecurityUtil.hashSenha(senhaPura);
        Usuario user = usuarioDAO.autenticar(login, senhaCripto);

        if (user != null) {
            ControleSessao.iniciarSessao(user);
            this.dispose();
            SwingUtilities.invokeLater(() -> new TelaDashboard().setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Acesso Negado: Credenciais Incorretas.", "Falha de Autenticação", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}