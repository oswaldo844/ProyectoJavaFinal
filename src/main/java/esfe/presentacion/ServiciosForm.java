package esfe.presentacion;

import esfe.dominio.Service;
import esfe.persistencia.ServiceDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Clase que representa el formulario de gestión de servicios en la interfaz gráfica
public class ServiciosForm {
    private JTextField txtNombre;         // Campo para el nombre del servicio
    private JTextField txtDuracion;       // Campo para la duración del servicio en minutos
    private JTextField txtPrecio;         // Campo para el precio del servicio
    private JCheckBox chkActivo;           // Checkbox para indicar si el servicio está activo
    private JTable tablaServicios;         // Tabla para mostrar la lista de servicios
    private JButton btnGuardar;            // Botón para guardar un nuevo servicio o actualizar uno existente
    private JButton btnEliminar;           // Botón para eliminar un servicio seleccionado
    private JButton btnCancelar;           // Botón para cerrar el formulario
    private JButton btnActualizar;         // Botón para cargar los datos de un servicio seleccionado en el formulario para su edición
    private JPanel mainPanel;              // Panel principal que contiene todos los componentes visuales

    private Integer idServicioEditando = null; // Variable que almacena el ID del servicio que se está editando; null si es nuevo

    // Constructor donde se inicializan acciones y estilos del formulario
    public ServiciosForm() {
        aplicarEstilo(); // Aplica estilos visuales personalizados a los componentes

        loadServices();  // Carga la lista de servicios desde la base de datos y la muestra en la tabla

        // Acción al presionar el botón Guardar
        btnGuardar.addActionListener(e -> {
            try {
                ServiceDAO dao = new ServiceDAO();

                // Convierte los valores de duración y precio ingresados en los campos de texto
                int duracion = Integer.parseInt(txtDuracion.getText());
                double precio = Double.parseDouble(txtPrecio.getText());

                // Crea un objeto Service con los datos del formulario
                Service service = new Service(
                        idServicioEditando != null ? idServicioEditando : 0,
                        txtNombre.getText(),
                        duracion,
                        precio,
                        chkActivo.isSelected()
                );

                // Si no se está editando un servicio existente, inserta uno nuevo
                if (idServicioEditando == null) {
                    dao.insertar(service);
                    JOptionPane.showMessageDialog(null, "Servicio guardado con éxito.");
                } else {
                    // Si se está editando, actualiza el servicio
                    dao.actualizar(service);
                    JOptionPane.showMessageDialog(null, "Servicio actualizado con éxito.");
                    idServicioEditando = null; // Resetea el modo edición
                }

                clearForm();  // Limpia los campos del formulario
                loadServices(); // Recarga la tabla con los datos actualizados
            } catch (NumberFormatException nfe) {
                // Manejo de error si los campos duración o precio no son números válidos
                JOptionPane.showMessageDialog(null, "Duración y Precio deben ser números válidos.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar o actualizar el servicio.");
            }
        });

        // Acción al presionar el botón Cancelar: cierra la ventana actual
        btnCancelar.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(mainPanel).dispose();
        });

        // Acción al presionar el botón Actualizar: carga datos del servicio seleccionado para editar
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

        // Acción al presionar el botón Eliminar: elimina el servicio seleccionado tras confirmación
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
                        loadServices();  // Recarga la lista después de eliminar
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

    // Método para obtener el panel principal (usado para agregar este formulario a un JFrame u otro contenedor)
    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Método para cargar los servicios desde la base de datos y mostrarlos en la tabla
    private void loadServices() {
        try {
            List<Service> services = new ServiceDAO().obtenerTodos(); // Obtiene todos los servicios (activos e inactivos)
            String[] columnNames = {"ID", "Nombre", "Duración (min)", "Precio", "Activo"};
            Object[][] data = new Object[services.size()][5];

            // Llena el arreglo con los datos de cada servicio
            for (int i = 0; i < services.size(); i++) {
                Service s = services.get(i);
                data[i][0] = s.getId();
                data[i][1] = s.getName();
                data[i][2] = s.getDurationMinutes();
                data[i][3] = s.getPrice();
                data[i][4] = s.isStatus();
            }

            // Configura el modelo de la tabla para mostrar los datos cargados
            tablaServicios.setModel(new DefaultTableModel(data, columnNames) {
                // Evita que las celdas puedan ser editadas directamente
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                // Define la clase de datos para cada columna (para mejor renderizado)
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

    // Limpia los campos del formulario para permitir ingresar un nuevo servicio
    private void clearForm() {
        txtNombre.setText("");
        txtDuracion.setText("");
        txtPrecio.setText("");
        chkActivo.setSelected(false);
        idServicioEditando = null; // Resetea el modo edición
    }

    // Aplica estilos visuales personalizados a los componentes del formulario para mejorar la UI
    private void aplicarEstilo() {
        // Color de fondo base del panel principal
        mainPanel.setBackground(new Color(245, 245, 250));

        // Colores y fuente para los botones
        Color botonColor = new Color(100, 149, 237);
        Color botonHover = new Color(65, 105, 225);
        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 14);
        Color textoBoton = Color.WHITE;

        // Aplica estilos y comportamiento hover a cada botón
        JButton[] botones = {btnGuardar, btnEliminar, btnCancelar, btnActualizar};
        for (JButton boton : botones) {
            if (boton != null) {
                boton.setBackground(botonColor);
                boton.setForeground(textoBoton);
                boton.setFont(fuenteBotones);
                boton.setFocusPainted(false);
                boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        // Estilos para campos de texto: fuente, color de fondo, color de texto y bordes
        JTextField[] camposTexto = {txtNombre, txtDuracion, txtPrecio};
        Font fuenteCampos = new Font("Segoe UI", Font.PLAIN, 14);
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

        // Estilo para el checkbox Activo
        if (chkActivo != null) {
            chkActivo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            chkActivo.setForeground(new Color(60, 60, 60));
            chkActivo.setBackground(mainPanel.getBackground());
        }

        // Estilos para la tabla de servicios: fuente, altura de fila, colores de selección y encabezado
        if (tablaServicios != null) {
            tablaServicios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tablaServicios.setRowHeight(26);
            tablaServicios.setSelectionBackground(new Color(100, 149, 237));
            tablaServicios.setSelectionForeground(Color.WHITE);
            tablaServicios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            tablaServicios.getTableHeader().setBackground(new Color(220, 220, 230));
            tablaServicios.getTableHeader().setForeground(new Color(70, 70, 70));
        }
    }
}
