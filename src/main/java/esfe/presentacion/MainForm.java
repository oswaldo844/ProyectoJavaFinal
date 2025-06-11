package esfe.presentacion;

import esfe.dominio.User;
import  javax.swing.*;
import java.awt.Toolkit;

public class MainForm extends JFrame{

    private User userAutenticate;

    public User getUserAutenticate() {
        return userAutenticate;
    }

    public void setUserAutenticate(User userAutenticate) {
        this.userAutenticate = userAutenticate;
    }

    public MainForm(){
        setTitle("Sistema en java de escritorio"); // Establece el título de la ventana principal (JFrame).
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura la operación por defecto al cerrar la ventana para que la aplicación se termine.
        setLocationRelativeTo(null); // Centra la ventana principal en la pantalla.
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Inicializa la ventana principal en estado maximizado, ocupando toda la pantalla.
        createMenu(); // Llama al método 'createMenu()' para crear y agregar la barra de menú a la ventana principal.
    }

    private void createMenu() {
        // Barra de menú
        JMenuBar menuBar = new JMenuBar(); // Crea una nueva barra de menú.
        setJMenuBar(menuBar); // Establece la barra de menú creada como la barra de menú de este JFrame (MainForm).

        JMenu menuPerfil = new JMenu("Perfil"); // Crea un nuevo menú llamado "Perfil".
        menuBar.add(menuPerfil); // Agrega el menú "Perfil" a la barra de menú.

        JMenuItem itemChangePassword = new JMenuItem("Cambiar contraseña"); // Crea un nuevo elemento de menú llamado "Cambiar contraseña".
        menuPerfil.add(itemChangePassword); // Agrega el elemento "Cambiar contraseña" al menú "Perfil".
        itemChangePassword.addActionListener(e -> { // Agrega un ActionListener al elemento "Cambiar contraseña".
            ChangePasswordForm changePassword = new ChangePasswordForm(this); // Cuando se hace clic, crea una nueva instancia de ChangePasswordForm, pasándole la instancia actual de MainForm como padre.
            changePassword.setVisible(true); // Hace visible la ventana de cambio de contraseña.

        });


        JMenuItem itemChangeUser = new JMenuItem("Cambiar de usuario"); // Crea un nuevo elemento de menú llamado "Cambiar de usuario".
        menuPerfil.add(itemChangeUser); // Agrega el elemento "Cambiar de usuario" al menú "Perfil".
        itemChangeUser.addActionListener(e -> { // Agrega un ActionListener al elemento "Cambiar de usuario".
            LoginForm loginForm = new LoginForm(this); // Cuando se hace clic, crea una nueva instancia de LoginForm (ventana de inicio de sesión), pasándole la instancia actual de MainForm como padre.
            loginForm.setVisible(true); // Hace visible la ventana de inicio de sesión.
        });


        JMenuItem itemSalir = new JMenuItem("Salir"); // Crea un nuevo elemento de menú llamado "Salir".
        menuPerfil.add(itemSalir); // Agrega el elemento "Salir" al menú "Perfil".
        itemSalir.addActionListener(e -> System.exit(0)); // Agrega un ActionListener al elemento "Salir". Cuando se hace clic, termina la ejecución de la aplicación (cierra la JVM).


        // Menú "Matenimiento"
        JMenu menuMantenimiento = new JMenu("Mantenimientos"); // Crea un nuevo menú llamado "Mantenimientos".
        menuBar.add(menuMantenimiento); // Agrega el menú "Mantenimientos" a la barra de menú.

        JMenuItem itemUsers = new JMenuItem("Usuarios"); // Crea un nuevo elemento de menú llamado "Usuarios".
        menuMantenimiento.add(itemUsers); // Agrega el elemento "Usuarios" al menú "Mantenimientos".
        itemUsers.addActionListener(e -> { // Agrega un ActionListener al elemento "Usuarios".
            UserReadingForm userReadingForm=new UserReadingForm(this); // Cuando se hace clic, crea una nueva instancia de UserReadingForm (formulario para leer/listar usuarios), pasándole la instancia actual de MainForm como padre.
            userReadingForm.setVisible(true); // Hace visible el formulario de lectura de usuarios.

        });
        JMenuItem itemClientes = new JMenuItem("Clientes");
        menuMantenimiento.add(itemClientes);
        itemClientes.addActionListener(e -> {
            ClientesForm clientesForm = new ClientesForm();

            JDialog dialog = new JDialog(this, "Gestión de Clientes", true);
            dialog.setContentPane(clientesForm.getmainPanel());

            // Establece tamaño a pantalla de cliente
            int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9);
            int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7);
            dialog.setSize(width, height);


            dialog.setLocationRelativeTo(this); // Centrado
            dialog.setVisible(true);
        });

// --- Aquí agregamos el menú y acción para Servicios ---
        JMenuItem itemServicios = new JMenuItem("Servicios");
        menuMantenimiento.add(itemServicios);
        itemServicios.addActionListener(e -> {
            ServiciosForm serviciosForm = new ServiciosForm();

            JDialog dialog = new JDialog(this, "Gestión de Servicios", true);
            dialog.setContentPane(serviciosForm.getMainPanel());

            // Tamaño similar al de Clientes
            int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9);
            int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7);
            dialog.setSize(width, height);

            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        // --- Aquí agregamos el menú y acción para Citas ---
        JMenuItem itemCitas = new JMenuItem("Citas");
        menuMantenimiento.add(itemCitas);
        itemCitas.addActionListener(e -> {
            // Creamos la instancia del formulario de Citas
            AppointmentForm appointmentForm = new AppointmentForm();

            // Creamos el diálogo modal
            JDialog dialog = new JDialog(this, "Gestión de Citas", true);
            dialog.setContentPane(appointmentForm.getMainPanel());

            // Ajustamos un tamaño similar al de Clientes/Servicios
            int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9);
            int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7);
            dialog.setSize(width, height);

            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);


        });

        //solucion supuestamente para corregir error de pantalla fuera

        JMenuItem itemListaCitas = new JMenuItem("Lista de Citas");
        menuMantenimiento.add(itemListaCitas);
        itemListaCitas.addActionListener(e -> {
            AppointmentListForm appointmentListForm = new AppointmentListForm();

            JDialog dialog = new JDialog(this, "Lista de Citas", true);
            dialog.setContentPane(appointmentListForm.getMainPanel());

            // Tamaño de la ventana
            int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9);
            int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7);
            dialog.setSize(width, height);

            dialog.setLocationRelativeTo(this); // Centrar respecto al MainForm
            dialog.setVisible(true);
        });




    }

}
