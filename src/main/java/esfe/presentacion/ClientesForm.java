package esfe.presentacion;

import esfe.dominio.Client;
import esfe.persistencia.ClientDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientesForm {
    // Campos de texto para ingresar los datos del cliente
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    // Botones del formulario
    private JButton btnGuardar;
    private JPanel mainPanel;
    private JButton btnCancelar;
    private JButton btnEdit;
    private JButton btnDelete;
    private JTable tablaClientes;

    // Almacena el ID del cliente que se está editando, si aplica
    private Integer idClienteEditando = null;

    public ClientesForm() {
        aplicarEstilo();  // Aplica estilos visuales a los componentes

        loadClients(); // Carga los clientes en la tabla al iniciar el formulario

        // Acción al presionar el botón Guardar
        btnGuardar.addActionListener(e -> {
            // Validar que todos los campos estén completos
            if (txtNombre.getText().isEmpty() || txtTelefono.getText().isEmpty() || txtEmail.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor completa todos los campos.");
                return;
            }

            try {
                ClientDAO dao = new ClientDAO();
                Client client = new Client(
                        idClienteEditando != null ? idClienteEditando : 0, // ID para actualizar, o 0 si es nuevo
                        txtNombre.getText(),
                        txtTelefono.getText(),
                        txtEmail.getText()
                );

                // Inserta o actualiza el cliente según el contexto
                if (idClienteEditando == null) {
                    dao.insertClient(client);
                    JOptionPane.showMessageDialog(null, "Cliente guardado con éxito.");
                } else {
                    dao.updateClient(client);
                    JOptionPane.showMessageDialog(null, "Cliente actualizado con éxito.");
                    idClienteEditando = null;
                }

                clearForm();    // Limpia los campos
                loadClients(); // Recarga la tabla
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar o actualizar el cliente.");
            }
        });

        // Acción al presionar el botón Cancelar
        btnCancelar.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(mainPanel).dispose(); // Cierra la ventana actual
        });

        // Acción al presionar el botón Editar
        btnEdit.addActionListener(e -> {
            int row = tablaClientes.getSelectedRow();
            if (row >= 0) {
                // Obtiene los datos de la fila seleccionada para editarlos
                idClienteEditando = (Integer) tablaClientes.getValueAt(row, 0);
                txtNombre.setText((String) tablaClientes.getValueAt(row, 1));
                txtTelefono.setText((String) tablaClientes.getValueAt(row, 2));
                txtEmail.setText((String) tablaClientes.getValueAt(row, 3));
                btnGuardar.setText("Actualizar");
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un cliente para editar.");
            }
        });

        // Acción al presionar el botón Eliminar
        btnDelete.addActionListener(e -> {
            int row = tablaClientes.getSelectedRow();
            if (row >= 0) {
                // Confirmación antes de eliminar
                int confirm = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de eliminar este cliente?", "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (Integer) tablaClientes.getValueAt(row, 0);
                    try {
                        new ClientDAO().deleteClient(id);
                        loadClients(); // Recarga la tabla
                        JOptionPane.showMessageDialog(null, "Cliente eliminado con éxito.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al eliminar cliente.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un cliente para eliminar.");
            }
        });
    }

    // Método para aplicar estilos personalizados a los componentes de la interfaz
    private void aplicarEstilo() {
        mainPanel.setBackground(new Color(245, 245, 250)); // Color de fondo claro

        Color botonColor = new Color(100, 149, 237); // Azul principal
        Color botonHover = new Color(65, 105, 225);  // Azul al pasar el mouse
        Color textoBoton = Color.WHITE;

        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 14);
        Font fuenteCampos = new Font("Segoe UI", Font.PLAIN, 14);

        // Aplica estilos a los botones
        JButton[] botones = {btnGuardar, btnCancelar, btnEdit, btnDelete};
        for (JButton boton : botones) {
            if (boton != null) {
                boton.setBackground(botonColor);
                boton.setForeground(textoBoton);
                boton.setFont(fuenteBotones);
                boton.setFocusPainted(false);
                boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // Efecto hover al pasar el mouse
                boton.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        boton.setBackground(botonHover);
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        boton.setBackground(botonColor);
                    }
                });
            }
        }

        // Estilo para los campos de texto
        JTextField[] camposTexto = {txtNombre, txtTelefono, txtEmail};
        for (JTextField campo : camposTexto) {
            if (campo != null) {
                campo.setFont(fuenteCampos);
                campo.setBackground(Color.WHITE);
                campo.setForeground(Color.DARK_GRAY);
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 190)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
            }
        }

        // Estilo para la tabla
        if (tablaClientes != null) {
            tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tablaClientes.setRowHeight(26);
            tablaClientes.setSelectionBackground(new Color(100, 149, 237));
            tablaClientes.setSelectionForeground(Color.WHITE);
            tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            tablaClientes.getTableHeader().setBackground(new Color(220, 220, 230));
            tablaClientes.getTableHeader().setForeground(new Color(70, 70, 70));
        }
    }

    // Devuelve el panel principal para agregarlo en el JFrame
    public JPanel getmainPanel() {
        return mainPanel;
    }

    // Carga los clientes desde la base de datos y los muestra en la tabla
    private void loadClients() {
        try {
            List<Client> clients = new ClientDAO().getAllClients();
            String[] columnNames = {"ID", "Nombre", "Teléfono", "Email"};
            Object[][] data = new Object[clients.size()][4];

            for (int i = 0; i < clients.size(); i++) {
                Client c = clients.get(i);
                data[i][0] = c.getId();
                data[i][1] = c.getName();
                data[i][2] = c.getPhone();
                data[i][3] = c.getEmail();
            }

            // Establece el modelo de la tabla con los datos obtenidos
            tablaClientes.setModel(new DefaultTableModel(data, columnNames) {
                public boolean isCellEditable(int row, int column) {
                    return false; // Evita que las celdas sean editables directamente
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la lista de clientes.");
        }
    }

    // Limpia los campos del formulario
    private void clearForm() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        idClienteEditando = null;
        btnGuardar.setText("Guardar"); // Restaura el texto original del botón
        txtNombre.requestFocus();
    }
}
