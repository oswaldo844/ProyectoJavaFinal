package esfe.presentacion;

import esfe.dominio.Service;
import esfe.persistencia.ServiceDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;




public class ServiciosForm {
    private JTextField txtNombre;
    private JTextField txtDuracion;
    private JTextField txtPrecio;
    private JCheckBox chkActivo;
    private JTable tablaServicios;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnCancelar;
    private JButton btnActualizar;
    private JPanel mainPanel;

    private Integer idServicioEditando = null; // Para modo edición

    public ServiciosForm() {
        loadServices();

        // Guardar (insertar o actualizar)
        btnGuardar.addActionListener(e -> {
            try {
                ServiceDAO dao = new ServiceDAO();

                // Validar que duración y precio sean números
                int duracion = Integer.parseInt(txtDuracion.getText());
                double precio = Double.parseDouble(txtPrecio.getText());

                Service service = new Service(
                        idServicioEditando != null ? idServicioEditando : 0,
                        txtNombre.getText(),
                        duracion,
                        precio,
                        chkActivo.isSelected()
                );

                if (idServicioEditando == null) {
                    dao.insertar(service);
                    JOptionPane.showMessageDialog(null, "Servicio guardado con éxito.");
                } else {
                    dao.actualizar(service);
                    JOptionPane.showMessageDialog(null, "Servicio actualizado con éxito.");
                    idServicioEditando = null;
                }

                clearForm();
                loadServices();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Duración y Precio deben ser números válidos.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar o actualizar el servicio.");
            }
        });

        // Botón Cancelar
        btnCancelar.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(mainPanel).dispose(); // Cierra la ventana
        });


        // Actualizar formulario con la fila seleccionada
        btnActualizar.addActionListener(e -> {
            int row = tablaServicios.getSelectedRow();
            if (row >= 0) {
                idServicioEditando = (Integer) tablaServicios.getValueAt(row, 0);
                txtNombre.setText((String) tablaServicios.getValueAt(row, 1));
                txtDuracion.setText(String.valueOf(tablaServicios.getValueAt(row, 2)));
                txtPrecio.setText(String.valueOf(tablaServicios.getValueAt(row, 3)));
                chkActivo.setSelected((Boolean) tablaServicios.getValueAt(row, 4));
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un servicio para actualizar.");
            }
        });

        // Eliminar servicio
        btnEliminar.addActionListener(e -> {
            int row = tablaServicios.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de eliminar este servicio?", "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (Integer) tablaServicios.getValueAt(row, 0);
                    try {
                        new ServiceDAO().eliminar(id);
                        loadServices();
                        JOptionPane.showMessageDialog(null, "Servicio eliminado con éxito.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al eliminar servicio.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un servicio para eliminar.");
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void loadServices() {
        try {
            List<Service> services = new ServiceDAO().obtenerTodos();
            String[] columnNames = {"ID", "Nombre", "Duración (min)", "Precio", "Activo"};
            Object[][] data = new Object[services.size()][5];

            for (int i = 0; i < services.size(); i++) {
                Service s = services.get(i);
                data[i][0] = s.getId();
                data[i][1] = s.getName();
                data[i][2] = s.getDurationMinutes();
                data[i][3] = s.getPrice();
                data[i][4] = s.isStatus();
            }

            tablaServicios.setModel(new DefaultTableModel(data, columnNames) {
                public boolean isCellEditable(int row, int column) {
                    return false; // No editable en la tabla
                }

                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 0) return Integer.class;
                    if (columnIndex == 2) return Integer.class;
                    if (columnIndex == 3) return Double.class;
                    if (columnIndex == 4) return Boolean.class;
                    return String.class;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la lista de servicios.");
        }
    }

    private void clearForm() {
        txtNombre.setText("");
        txtDuracion.setText("");
        txtPrecio.setText("");
        chkActivo.setSelected(false);
        idServicioEditando = null;
    }
}

