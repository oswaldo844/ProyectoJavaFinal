package esfe.presentacion;

import esfe.presentacion.AppointmentListForm;


import javax.swing.SpinnerDateModel;
import javax.swing.JSpinner;
import java.util.Calendar;
import java.util.Date;

import esfe.persistencia.AppointmentDAO;
import esfe.persistencia.ClientDAO;
import esfe.persistencia.ServiceDAO;
import esfe.dominio.Appointment;
import esfe.dominio.Client;
import esfe.dominio.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class AppointmentForm {
    private JComboBox cmbClients;
    private JComboBox cmbServices;

    private JTextArea txtNotes;
    private JCheckBox chkActive;
    private JButton btnSave;
    private JPanel mainPanel;
    private JTable tblCitas;
    private JSpinner spinDateTime;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnCancel;
    private JButton btnViewAppointments;


    private Integer selectedAppointmentId = null;

    public AppointmentForm() {
        // Spinner configuración
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        spinDateTime.setModel(model);
        spinDateTime.setEditor(new JSpinner.DateEditor(spinDateTime, "yyyy-MM-dd HH:mm"));

        // Cargar datos iniciales
        loadClients();
        loadServices();
        loadAppointments();

        // Listeners
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
    }
    private void openAppointmentListForm() {
        AppointmentListForm form = new AppointmentListForm();
        JPanel listadoPanel = form.getMainPanel();

        // Reemplazar el contenido del panel principal
        mainPanel.removeAll();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(listadoPanel);

        // Refrescar el panel
        mainPanel.revalidate();
        mainPanel.repaint();
    }



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

    private void saveAppointment() {
        if (cmbClients.getSelectedItem() == null || cmbServices.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(mainPanel, "Selecciona un cliente y un servicio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Client client = (Client) cmbClients.getSelectedItem();
            Service service = (Service) cmbServices.getSelectedItem();
            Date date = (Date) spinDateTime.getValue();

            Appointment a = new Appointment();
            a.setClientId(client.getId());
            a.setServiceId(service.getId());
            a.setDate(date);
            a.setNotes(txtNotes.getText().trim());
            a.setStatus(chkActive.isSelected() ? 1 : 0);

            new AppointmentDAO().addAppointment(a);
            JOptionPane.showMessageDialog(mainPanel, "✅ Cita guardada correctamente.");

            loadAppointments();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al guardar la cita:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    private void cancelAppointment() {
        if (selectedAppointmentId == null) {
            JOptionPane.showMessageDialog(mainPanel, "Selecciona una cita para cancelar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(mainPanel, "¿Cancelar la cita seleccionada?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                List<Appointment> list = new AppointmentDAO().getAllAppointments();
                for (Appointment a : list) {
                    if (a.getId() == selectedAppointmentId) {
                        a.setStatus(0);
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

    private void loadSelectedAppointmentToForm() {
        int selectedRow = tblCitas.getSelectedRow();
        if (selectedRow >= 0) {
            selectedAppointmentId = (Integer) tblCitas.getValueAt(selectedRow, 0);
            String clientName = (String) tblCitas.getValueAt(selectedRow, 1);
            String serviceName = (String) tblCitas.getValueAt(selectedRow, 2);
            String dateStr = (String) tblCitas.getValueAt(selectedRow, 3);
            String notes = (String) tblCitas.getValueAt(selectedRow, 4);
            String statusStr = (String) tblCitas.getValueAt(selectedRow, 5);

            for (int i = 0; i < cmbClients.getItemCount(); i++) {
                Client c = (Client) cmbClients.getItemAt(i);
                if (c.getName().equals(clientName)) {
                    cmbClients.setSelectedIndex(i);
                    break;
                }
            }

            for (int i = 0; i < cmbServices.getItemCount(); i++) {
                Service s = (Service) cmbServices.getItemAt(i);
                if (s.getName().equals(serviceName)) {
                    cmbServices.setSelectedIndex(i);
                    break;
                }
            }

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr);
                spinDateTime.setValue(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            txtNotes.setText(notes);
            chkActive.setSelected("Activa".equalsIgnoreCase(statusStr));
        } else {
            selectedAppointmentId = null;
        }
    }

    private void clearForm() {
        cmbClients.setSelectedIndex(-1);
        cmbServices.setSelectedIndex(-1);
        spinDateTime.setValue(new Date());
        txtNotes.setText("");
        chkActive.setSelected(true);
        tblCitas.clearSelection();
        selectedAppointmentId = null;
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}