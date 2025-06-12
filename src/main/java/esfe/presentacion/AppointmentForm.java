package esfe.presentacion;

import esfe.persistencia.AppointmentDAO;
import esfe.persistencia.ClientDAO;
import esfe.persistencia.ServiceDAO;
import esfe.dominio.Appointment;
import esfe.dominio.Client;
import esfe.dominio.Service;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.SpinnerDateModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AppointmentForm {
    // Componentes gráficos
    private JComboBox cmbClients;   // ComboBox para seleccionar clientes
    private JComboBox cmbServices;  // ComboBox para seleccionar servicios
    private JTextArea txtNotes;     // Área de texto para notas de la cita
    private JCheckBox chkActive;    // Checkbox para marcar si la cita está activa
    private JButton btnSave;        // Botón para guardar nueva cita
    private JPanel mainPanel;       // Panel principal del formulario
    private JTable tblCitas;        // Tabla para mostrar las citas registradas
    private JSpinner spinDateTime;  // Spinner para seleccionar fecha y hora
    private JButton btnEdit;        // Botón para editar una cita
    private JButton btnDelete;      // Botón para eliminar una cita
    private JButton btnCancel;      // Botón para cancelar una cita
    private JButton btnViewAppointments;  // Botón para ver todas las citas en otro formulario

    // Variable para almacenar el ID de la cita seleccionada en la tabla
    private Integer selectedAppointmentId = null;

    // Constructor donde se crea toda la interfaz y se configuran los eventos
    public AppointmentForm() {
        // Crear el panel principal con diseño vertical y márgenes
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configurar el spinner para fecha y hora con formato específico
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        spinDateTime = new JSpinner(model);
        spinDateTime.setEditor(new JSpinner.DateEditor(spinDateTime, "yyyy-MM-dd HH:mm"));

        // Inicializar componentes
        cmbClients = new JComboBox();
        cmbServices = new JComboBox();
        txtNotes = new JTextArea(3, 20);
        chkActive = new JCheckBox("Cita Activa");
        chkActive.setSelected(true); // Por defecto, la cita está activa

        btnSave = new JButton("Guardar");
        btnEdit = new JButton("Editar");
        btnDelete = new JButton("Eliminar");
        btnCancel = new JButton("Cancelar");
        btnViewAppointments = new JButton("Ver Todas");

        tblCitas = new JTable();

        // Crear panel para seleccionar cliente y servicio, con etiquetas
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Información de la cita"));
        topPanel.add(new JLabel("Cliente:"));
        topPanel.add(cmbClients);
        topPanel.add(new JLabel("Servicio:"));
        topPanel.add(cmbServices);

        // Panel para fecha/hora y notas
        JPanel middlePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        middlePanel.setBorder(BorderFactory.createTitledBorder("Detalles de la cita"));
        middlePanel.add(new JLabel("Fecha y Hora:"));
        middlePanel.add(spinDateTime);
        middlePanel.add(new JLabel("Notas:"));
        middlePanel.add(new JScrollPane(txtNotes)); // Scroll para el área de texto

        // Panel para checkbox estado de la cita
        JPanel statusPanel = new JPanel();
        statusPanel.add(chkActive);

        // Panel para los botones (guardar, editar, eliminar, etc)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnViewAppointments);

        // Panel para la tabla de citas con título y scroll
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Citas Registradas"));
        tablePanel.add(new JScrollPane(tblCitas));

        // Agregar todos los paneles al panel principal con espacios verticales
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(middlePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(statusPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(tablePanel);

        // Configurar eventos de botones y selección de tabla
        btnSave.addActionListener(e -> saveAppointment());
        btnEdit.addActionListener(e -> editAppointment());
        btnDelete.addActionListener(e -> deleteAppointment());
        btnCancel.addActionListener(e -> cancelAppointment());
        btnViewAppointments.addActionListener(e -> openAppointmentListForm());

        tblCitas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedAppointmentToForm();
            }
        });

        // Cargar datos iniciales en combos y tabla
        loadClients();
        loadServices();
        loadAppointments();
    }

    // Abre otro formulario para ver todas las citas
    private void openAppointmentListForm() {
        AppointmentListForm form = new AppointmentListForm();
        JPanel listadoPanel = form.getMainPanel();

        mainPanel.removeAll();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(listadoPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Carga la lista de clientes desde la base de datos en el combo
    private void loadClients() {
        try {
            cmbClients.removeAllItems();
            List<Client> clients = new ClientDAO().getAllClients();
            for (Client c : clients) {
                cmbClients.addItem(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al cargar clientes:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Carga la lista de servicios desde la base de datos en el combo
    private void loadServices() {
        try {
            cmbServices.removeAllItems();
            List<Service> services = new ServiceDAO().getAllServices();
            for (Service s : services) {
                cmbServices.addItem(s);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al cargar servicios:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Carga todas las citas desde la base de datos y las muestra en la tabla
    private void loadAppointments() {
        try {
            List<Appointment> list = new AppointmentDAO().getAllAppointments();
            String[] columns = {"ID", "Cliente", "Servicio", "Fecha", "Notas", "Estado"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            for (Appointment a : list) {
                Object[] row = {
                        a.getId(),
                        a.getClientName(),
                        a.getServiceName(),
                        sdf.format(a.getDate()),
                        a.getNotes(),
                        a.getStatus() == 1 ? "Activa" : "Cancelada"
                };
                model.addRow(row);
            }

            tblCitas.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al cargar citas:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Guarda una nueva cita en la base de datos con los datos del formulario
    private void saveAppointment() {
        // Validar que haya cliente y servicio seleccionados
        if (cmbClients.getSelectedItem() == null || cmbServices.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(mainPanel, "Selecciona un cliente y un servicio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Client client = (Client) cmbClients.getSelectedItem();
            Service service = (Service) cmbServices.getSelectedItem();
            Date date = (Date) spinDateTime.getValue();

            // Crear objeto Appointment y asignar valores
            Appointment a = new Appointment();
            a.setClientId(client.getId());
            a.setServiceId(service.getId());
            a.setDate(date);
            a.setNotes(txtNotes.getText().trim());
            a.setStatus(chkActive.isSelected() ? 1 : 0);

            // Insertar en base de datos
            new AppointmentDAO().addAppointment(a);
            JOptionPane.showMessageDialog(mainPanel, "✅ Cita guardada correctamente.");
            loadAppointments(); // Refrescar tabla
            clearForm();       // Limpiar formulario
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al guardar la cita:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edita la cita seleccionada con los datos del formulario
    private void editAppointment() {
        if (selectedAppointmentId == null) {
            JOptionPane.showMessageDialog(mainPanel, "Selecciona una cita para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Client client = (Client) cmbClients.getSelectedItem();
            Service service = (Service) cmbServices.getSelectedItem();
            Date date = (Date) spinDateTime.getValue();

            Appointment a = new Appointment();
            a.setId(selectedAppointmentId);
            a.setClientId(client.getId());
            a.setServiceId(service.getId());
            a.setDate(date);
            a.setNotes(txtNotes.getText().trim());
            a.setStatus(chkActive.isSelected() ? 1 : 0);

            new AppointmentDAO().updateAppointment(a);
            JOptionPane.showMessageDialog(mainPanel, "✅ Cita actualizada correctamente.");
            loadAppointments();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al actualizar la cita:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Elimina la cita seleccionada
    private void deleteAppointment() {
        if (selectedAppointmentId == null) {
            JOptionPane.showMessageDialog(mainPanel, "Selecciona una cita para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(mainPanel, "¿Eliminar la cita seleccionada?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new AppointmentDAO().deleteAppointment(selectedAppointmentId);
                JOptionPane.showMessageDialog(mainPanel, "✅ Cita eliminada correctamente.");
                loadAppointments();
                clearForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainPanel, "Error al eliminar la cita:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Cancela la cita seleccionada (cambia estado a cancelado)
    private void cancelAppointment() {
        if (selectedAppointmentId == null) {
            JOptionPane.showMessageDialog(mainPanel, "Selecciona una cita para cancelar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(mainPanel, "¿Cancelar la cita seleccionada?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Buscar la cita y cambiar su estado
                List<Appointment> list = new AppointmentDAO().getAllAppointments();
                for (Appointment a : list) {
                    if (a.getId() == selectedAppointmentId) {
                        a.setStatus(0);  // Estado cancelado
                        new AppointmentDAO().updateAppointment(a);
                        break;
                    }
                }

                JOptionPane.showMessageDialog(mainPanel, "✅ Cita cancelada correctamente.");
                loadAppointments();
                clearForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainPanel, "Error al cancelar la cita:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Carga los datos de la cita seleccionada en la tabla a los controles del formulario
    private void loadSelectedAppointmentToForm() {
        int selectedRow = tblCitas.getSelectedRow();
        if (selectedRow >= 0) {
            selectedAppointmentId = (Integer) tblCitas.getValueAt(selectedRow, 0);  // ID cita
            String clientName = (String) tblCitas.getValueAt(selectedRow, 1);
            String serviceName = (String) tblCitas.getValueAt(selectedRow, 2);
            String dateStr = (String) tblCitas.getValueAt(selectedRow, 3);
            String notes = (String) tblCitas.getValueAt(selectedRow, 4);
            String statusStr = (String) tblCitas.getValueAt(selectedRow, 5);

            // Seleccionar cliente y servicio en combos según nombre (podrías buscar mejor por ID si lo tienes)
            selectComboItemByName(cmbClients, clientName);
            selectComboItemByName(cmbServices, serviceName);

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = sdf.parse(dateStr);
                spinDateTime.setValue(date);
            } catch (Exception e) {
                spinDateTime.setValue(new Date());
            }

            txtNotes.setText(notes);
            chkActive.setSelected(statusStr.equalsIgnoreCase("Activa"));
        } else {
            selectedAppointmentId = null;
        }
    }

    // Método auxiliar para seleccionar un ítem en un JComboBox basado en el nombre (toString)
    private void selectComboItemByName(JComboBox combo, String name) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            if (item.toString().equals(name)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.setSelectedIndex(-1);
    }

    // Limpia el formulario
    private void clearForm() {
        cmbClients.setSelectedIndex(-1);
        cmbServices.setSelectedIndex(-1);
        spinDateTime.setValue(new Date());
        txtNotes.setText("");
        chkActive.setSelected(true);
        selectedAppointmentId = null;
        tblCitas.clearSelection();
    }

    // Devuelve el panel principal para agregarlo a un JFrame o JDialog
    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Para probar el formulario independiente
    public static void main(String[] args) {
        JFrame frame = new JFrame("Formulario de Citas");
        frame.setContentPane(new AppointmentForm().getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
