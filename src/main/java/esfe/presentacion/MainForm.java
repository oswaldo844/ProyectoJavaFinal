package esfe.presentacion;

import esfe.dominio.User;
import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {

    private User userAutenticate;

    public User getUserAutenticate() {
        return userAutenticate;
    }

    public void setUserAutenticate(User userAutenticate) {
        this.userAutenticate = userAutenticate;
    }

    public MainForm() {
        setTitle("Sistema en Java de Escritorio - Barbería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Panel de fondo
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245)); // Color claro de fondo

        // Mensaje de bienvenida
        JLabel label = new JLabel("¡Bienvenido al Sistema de Gestión de Citas de la Barbería!", JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
        label.setForeground(new Color(54, 33, 89));
        contentPanel.add(label, BorderLayout.CENTER);

        setContentPane(contentPanel); // Establece este panel como contenido principal

        createMenu(); // Menú
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(54, 33, 89)); // Morado oscuro
        menuBar.setForeground(Color.WHITE);
        setJMenuBar(menuBar);

        JMenu menuPerfil = new JMenu("Perfil");
        menuPerfil.setForeground(Color.WHITE);
        menuBar.add(menuPerfil);

        JMenuItem itemChangePassword = new JMenuItem("Cambiar contraseña");
        menuPerfil.add(itemChangePassword);
        itemChangePassword.addActionListener(e -> {
            ChangePasswordForm changePassword = new ChangePasswordForm(this);
            changePassword.setVisible(true);
        });

        JMenuItem itemChangeUser = new JMenuItem("Cambiar de usuario");
        menuPerfil.add(itemChangeUser);
        itemChangeUser.addActionListener(e -> {
            LoginForm loginForm = new LoginForm(this);
            loginForm.setVisible(true);
        });

        JMenuItem itemSalir = new JMenuItem("Salir");
        menuPerfil.add(itemSalir);
        itemSalir.addActionListener(e -> System.exit(0));

        JMenu menuMantenimiento = new JMenu("Mantenimientos");
        menuMantenimiento.setForeground(Color.WHITE);
        menuBar.add(menuMantenimiento);

        JMenuItem itemUsers = new JMenuItem("Usuarios");
        menuMantenimiento.add(itemUsers);
        itemUsers.addActionListener(e -> {
            UserReadingForm userReadingForm = new UserReadingForm(this);
            userReadingForm.setVisible(true);
        });

        JMenuItem itemClientes = new JMenuItem("Clientes");
        menuMantenimiento.add(itemClientes);
        itemClientes.addActionListener(e -> {
            ClientesForm clientesForm = new ClientesForm();
            JDialog dialog = new JDialog(this, "Gestión de Clientes", true);
            dialog.setContentPane(clientesForm.getmainPanel());

            int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9);
            int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7);
            dialog.setSize(width, height);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        JMenuItem itemServicios = new JMenuItem("Servicios");
        menuMantenimiento.add(itemServicios);
        itemServicios.addActionListener(e -> {
            ServiciosForm serviciosForm = new ServiciosForm();
            JDialog dialog = new JDialog(this, "Gestión de Servicios", true);
            dialog.setContentPane(serviciosForm.getMainPanel());
            int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9);
            int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7);
            dialog.setSize(width, height);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        JMenuItem itemCitas = new JMenuItem("Citas");
        menuMantenimiento.add(itemCitas);
        itemCitas.addActionListener(e -> {
            AppointmentForm appointmentForm = new AppointmentForm();
            JDialog dialog = new JDialog(this, "Gestión de Citas", true);
            dialog.setContentPane(appointmentForm.getMainPanel());
            int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9);
            int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7);
            dialog.setSize(width, height);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        JMenuItem itemListaCitas = new JMenuItem("Lista de Citas");
        menuMantenimiento.add(itemListaCitas);
        itemListaCitas.addActionListener(e -> {
            AppointmentListForm appointmentListForm = new AppointmentListForm();
            JDialog dialog = new JDialog(this, "Lista de Citas", true);
            dialog.setContentPane(appointmentListForm.getMainPanel());
            int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9);
            int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7);
            dialog.setSize(width, height);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });
    }
}
