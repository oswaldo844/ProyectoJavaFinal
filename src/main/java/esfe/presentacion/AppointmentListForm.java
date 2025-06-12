package esfe.presentacion;

import com.toedter.calendar.JDateChooser;
import esfe.dominio.Appointment;
import esfe.persistencia.AppointmentDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppointmentListForm {
    // Componentes de interfaz
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
        // Configuración del panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(250, 250, 250));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel superior con filtros
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setBackground(new Color(240, 240, 240));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtrar Citas"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicialización de componentes
        cmbEstado = new JComboBox();
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbEstado.addItem("Todas");
        cmbEstado.addItem("Activa");
        cmbEstado.addItem("Cancelada");

        // Inicializar paneles para seleccionar fechas
        panelFechaInicio = new JPanel(new BorderLayout());
        panelFechaFin = new JPanel(new BorderLayout());
        dateChooserInicio = new JDateChooser();
        dateChooserFin = new JDateChooser();
        panelFechaInicio.add(dateChooserInicio, BorderLayout.CENTER);
        panelFechaFin.add(dateChooserFin, BorderLayout.CENTER);

        // Botones para filtrar y mostrar todas las citas
        btnFiltrar = new JButton("Filtrar");
        btnMostrarTodas = new JButton("Mostrar Todas");

        // Etiquetas
        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblDesde = new JLabel("Desde:");
        JLabel lblHasta = new JLabel("Hasta:");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesde.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblHasta.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Añadir etiquetas y componentes al panel de filtros con GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFiltros.add(lblEstado, gbc);

        gbc.gridx = 1;
        panelFiltros.add(cmbEstado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFiltros.add(lblDesde, gbc);

        gbc.gridx = 1;
        panelFiltros.add(panelFechaInicio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFiltros.add(lblHasta, gbc);

        gbc.gridx = 1;
        panelFiltros.add(panelFechaFin, gbc);

        // Botones centrados
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBackground(new Color(240, 240, 240));
        panelBotones.add(btnFiltrar);
        panelBotones.add(btnMostrarTodas);
        panelFiltros.add(panelBotones, gbc);

        // Tabla para mostrar las citas
        tblCitasFiltradas = new JTable();
        tblCitasFiltradas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblCitasFiltradas.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(tblCitasFiltradas);

        // Agregar los paneles al panel principal
        mainPanel.add(panelFiltros, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Configurar eventos de los botones
        btnFiltrar.addActionListener(e -> filtrarCitas());
        btnMostrarTodas.addActionListener(e -> cargarTodasLasCitas());

        // Mostrar todas las citas al iniciar
        cargarTodasLasCitas();
    }

    // Método para obtener el panel principal y agregarlo a una ventana
    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Carga todas las citas sin aplicar filtros
    private void cargarTodasLasCitas() {
        mostrarCitas(null, null, "Todas");
    }

    // Valida fechas y aplica filtros de fecha y estado
    private void filtrarCitas() {
        Date fechaInicio = dateChooserInicio.getDate();
        Date fechaFin = dateChooserFin.getDate();
        String estado = (String) cmbEstado.getSelectedItem();

        // Validación: la fecha fin no puede ser menor que la de inicio
        if (fechaInicio != null && fechaFin != null && fechaFin.before(fechaInicio)) {
            JOptionPane.showMessageDialog(mainPanel, "La fecha de fin no puede ser anterior a la de inicio.");
            return;
        }

        mostrarCitas(fechaInicio, fechaFin, estado);
    }

    // Muestra las citas en la tabla, aplicando filtros si se especifican
    private void mostrarCitas(Date fechaInicio, Date fechaFin, String estadoFiltro) {
        try {
            AppointmentDAO dao = new AppointmentDAO();
            List<Appointment> citas = dao.getAllAppointments();

            // Modelo para la tabla
            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"ID", "Cliente", "Servicio", "Fecha", "Notas", "Estado"}, 0
            );
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            for (Appointment cita : citas) {
                Date fecha = cita.getDate();
                String estado = cita.getStatus() == 1 ? "Activa" : "Cancelada";

                // Verifica si la cita está dentro del rango de fechas y coincide con el estado
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
