package esfe.presentacion;

import com.toedter.calendar.JDateChooser;
import esfe.dominio.Appointment;
import esfe.persistencia.AppointmentDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppointmentListForm {
    private JComboBox cmbEstado;
    private JPanel panelFechaInicio;
    private JPanel panelFechaFin;
    private JButton btnFiltrar;
    private JButton btnMostrarTodas;
    private JTable tblCitasFiltradas;
    private JPanel mainPanel;

    private JDateChooser dateChooserInicio;
    private JDateChooser dateChooserFin;

    public AppointmentListForm() {
        // Crear DateChoosers
        dateChooserInicio = new JDateChooser();
        dateChooserFin = new JDateChooser();

        // Añadir DateChoosers a los paneles
        if (panelFechaInicio != null) {
            panelFechaInicio.setLayout(new BorderLayout());
            panelFechaInicio.add(dateChooserInicio, BorderLayout.CENTER);
        }

        if (panelFechaFin != null) {
            panelFechaFin.setLayout(new BorderLayout());
            panelFechaFin.add(dateChooserFin, BorderLayout.CENTER);
        }

        // Asegurarse que el combo tiene opciones
        if (cmbEstado != null) {
            cmbEstado.addItem("Todas");
            cmbEstado.addItem("Activa");
            cmbEstado.addItem("Cancelada");
        }

        // Acciones
        if (btnFiltrar != null) {
            btnFiltrar.addActionListener(e -> filtrarCitas());
        }

        if (btnMostrarTodas != null) {
            btnMostrarTodas.addActionListener(e -> cargarTodasLasCitas());
        }

        cargarTodasLasCitas(); // Mostrar todas al inicio
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void cargarTodasLasCitas() {
        mostrarCitas(null, null, "Todas");
    }

    private void filtrarCitas() {
        Date fechaInicio = dateChooserInicio.getDate();
        Date fechaFin = dateChooserFin.getDate();
        String estado = (String) cmbEstado.getSelectedItem();

        if (fechaInicio != null && fechaFin != null && fechaFin.before(fechaInicio)) {
            JOptionPane.showMessageDialog(mainPanel, "La fecha de fin no puede ser anterior a la de inicio.");
            return;
        }

        mostrarCitas(fechaInicio, fechaFin, estado);
    }

    private void mostrarCitas(Date fechaInicio, Date fechaFin, String estadoFiltro) {
        try {
            AppointmentDAO dao = new AppointmentDAO();
            List<Appointment> citas = dao.getAllAppointments();

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"ID", "Cliente", "Servicio", "Fecha", "Notas", "Estado"}, 0
            );
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            for (Appointment cita : citas) {
                Date fecha = cita.getDate();
                String estado = cita.getStatus() == 1 ? "Activa" : "Cancelada";

                boolean dentroRango = (fechaInicio == null || !fecha.before(fechaInicio)) &&
                        (fechaFin == null || !fecha.after(fechaFin));
                boolean estadoCoincide = estadoFiltro.equals("Todas") || estado.equals(estadoFiltro);

                if (dentroRango && estadoCoincide) {
                    model.addRow(new Object[]{
                            cita.getId(),
                            cita.getClientName(),
                            cita.getServiceName(),
                            sdf.format(cita.getDate()),
                            cita.getNotes(),
                            estado
                    });
                }
            }

            tblCitasFiltradas.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, "Error al cargar citas: " + ex.getMessage());
        }
    }
}
