package esfe.presentacion;

import esfe.dominio.User;
import esfe.persistencia.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class LoginForm  extends JDialog {
    private JPanel mainPanel;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;

    private UserDAO userDAO; // Declaración de una variable de instancia llamada 'userDAO' de tipo UserDAO. Esta variable se utilizará para interactuar con la capa de acceso a datos de los usuarios (por ejemplo, para autenticar usuarios).
    private MainForm mainForm; // Declaración de una variable de instancia llamada 'mainForm' de tipo MainForm. Esta variable  representa la ventana principal de la aplicación y se utiliza para interactuar con ella (por ejemplo, para pasar información del usuario autenticado).

    public LoginForm(MainForm mainForm) {
        this.mainForm = mainForm;
        userDAO = new UserDAO();

        // DISEÑO DEL PANEL PRINCIPAL
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ETIQUETA TÍTULO
        JLabel lblTitulo = new JLabel("Inicio de Sesión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitulo, gbc);

        // LABEL EMAIL
        JLabel lblEmail = new JLabel("Correo electrónico:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmail.setForeground(Color.DARK_GRAY);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(lblEmail, gbc);

        // TEXTFIELD EMAIL
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(txtEmail, gbc);

        // LABEL PASSWORD
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(lblPassword, gbc);

        // PASSFIELD
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);

        // BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(new Color(245, 245, 245));
        btnLogin = new JButton("Iniciar Sesión");
        btnSalir = new JButton("Salir");

        panelBotones.add(btnLogin);
        panelBotones.add(btnSalir);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(panelBotones, gbc);

        // CONFIGURACIÓN DEL DIALOG
        setContentPane(mainPanel);
        setModal(true);
        setTitle("Login");
        pack();
        setResizable(false);
        setLocationRelativeTo(mainForm);

        // ACCIONES
        btnSalir.addActionListener(e -> System.exit(0));
        btnLogin.addActionListener(e -> login());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void login() {
        try {
            User user = new User();
            user.setEmail(txtEmail.getText());
            user.setPasswordHash(new String(txtPassword.getPassword()));

            User userAut = userDAO.authenticate(user);

            if (userAut != null && userAut.getId() > 0 && userAut.getEmail().equals((user.getEmail()))) {
                this.mainForm.setUserAutenticate(userAut);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Email y password incorrecto",
                        "Login",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Sistem",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}