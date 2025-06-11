package esfe.presentacion;

import esfe.dominio.Client;
import esfe.persistencia.ClientDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;;

public class ClientesForm {
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    private JButton btnGuardar;
    private JPanel mainPanel;
    private JButton btnCancelar;
    private JButton btnEdit;
    private JButton btnDelete;
    private JTable tablaClientes;


    private Integer idClienteEditando = null; // Para saber si se está editando

    public ClientesForm() {
        loadClients();

        // Botón Guardar (insertar o actualizar)
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().isEmpty() || txtTelefono.getText().isEmpty() || txtEmail.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor completa todos los campos.");
                return;
            }

            try {
                ClientDAO dao = new ClientDAO();
                Client client = new Client(idClienteEditando != null ? idClienteEditando : 0,
                        txtNombre.getText(), txtTelefono.getText(), txtEmail.getText());

                if (idClienteEditando == null) {
                    dao.insertClient(client);
                    JOptionPane.showMessageDialog(null, "Cliente guardado con éxito.");
                } else {
                    dao.updateClient(client);
                    JOptionPane.showMessageDialog(null, "Cliente actualizado con éxito.");
                    idClienteEditando = null;
                }

                clearForm();
                loadClients();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar o actualizar el cliente.");
            }
        });

        // Botón Cancelar
        btnCancelar.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(mainPanel).dispose(); // Cierra la ventana
        });

        // Botón Editar
        btnEdit.addActionListener(e -> {
            int row = tablaClientes.getSelectedRow();
            if (row >= 0) {
                idClienteEditando = (Integer) tablaClientes.getValueAt(row, 0);
                txtNombre.setText((String) tablaClientes.getValueAt(row, 1));
                txtTelefono.setText((String) tablaClientes.getValueAt(row, 2));
                txtEmail.setText((String) tablaClientes.getValueAt(row, 3));
                btnGuardar.setText("Actualizar");
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un cliente para editar.");
            }
        });

        // Botón Eliminar
        btnDelete.addActionListener(e -> {
            int row = tablaClientes.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de eliminar este cliente?", "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (Integer) tablaClientes.getValueAt(row, 0);
                    try {
                        new ClientDAO().deleteClient(id);
                        loadClients();
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

    public JPanel getmainPanel() {
        return mainPanel;
    }

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

            tablaClientes.setModel(new DefaultTableModel(data, columnNames) {
                public boolean isCellEditable(int row, int column) {
                    return false; // Evita edición directa
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la lista de clientes.");
        }
    }

    private void clearForm() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        idClienteEditando = null;
        btnGuardar.setText("Guardar");
        txtNombre.requestFocus();
    }
}