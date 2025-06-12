package esfe.persistencia;

import esfe.dominio.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para gestionar operaciones CRUD
 * sobre la tabla Appointments en la base de datos.
 */
public class AppointmentDAO {

    /**
     * Inserta una nueva cita en la base de datos.
     *
     * @param appt Objeto Appointment que contiene los datos de la nueva cita.
     */
    public void addAppointment(Appointment appt) {
        String sql = "INSERT INTO Appointments (client_id, service_id, date, notes, status) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = ConnectionManager.getInstance().connect(); // Establece la conexión
                PreparedStatement stmt = conn.prepareStatement(sql) // Prepara la consulta con parámetros
        ) {
            // Asigna los valores a los parámetros de la consulta
            stmt.setInt(1, appt.getClientId());
            stmt.setInt(2, appt.getServiceId());
            stmt.setTimestamp(3, new java.sql.Timestamp(appt.getDate().getTime()));
            stmt.setString(4, appt.getNotes());
            stmt.setInt(5, appt.getStatus());

            stmt.executeUpdate(); // Ejecuta la inserción

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo básico de errores
        }
    }

    /**
     * Recupera todas las citas desde la base de datos, incluyendo los nombres del cliente y servicio.
     *
     * @return Lista de objetos Appointment con datos de citas.
     */
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();

        // Consulta SQL que también une las tablas Clients y Services para obtener los nombres
        String sql = """
            SELECT a.id, a.client_id, a.service_id, a.date, a.notes, a.status,
                   c.name AS clientName,
                   s.name AS serviceName
            FROM Appointments a
            JOIN Clients c ON a.client_id = c.id
            JOIN Services s ON a.service_id = s.id
            """;

        try (
                Connection conn = ConnectionManager.getInstance().connect(); // Establece la conexión
                Statement stmt = conn.createStatement();                     // Crea la sentencia
                ResultSet rs = stmt.executeQuery(sql)                        // Ejecuta la consulta
        ) {

            // Itera sobre cada resultado y lo mapea a un objeto Appointment
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setClientId(rs.getInt("client_id"));
                a.setServiceId(rs.getInt("service_id"));
                a.setDate(rs.getTimestamp("date"));
                a.setNotes(rs.getString("notes"));
                a.setStatus(rs.getInt("status"));
                a.setClientName(rs.getString("clientName"));
                a.setServiceName(rs.getString("serviceName"));

                list.add(a); // Agrega la cita a la lista
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo básico de errores
        }

        return list; // Retorna la lista de citas
    }

    /**
     * Actualiza una cita existente en la base de datos.
     *
     * @param appt Objeto Appointment con los datos actualizados.
     */
    public void updateAppointment(Appointment appt) {
        String sql = """
            UPDATE Appointments
            SET client_id = ?, service_id = ?, date = ?, notes = ?, status = ?
            WHERE id = ?
            """;

        try (
                Connection conn = ConnectionManager.getInstance().connect(); // Establece la conexión
                PreparedStatement stmt = conn.prepareStatement(sql)          // Prepara la consulta
        ) {
            // Asigna los nuevos valores a la consulta
            stmt.setInt(1, appt.getClientId());
            stmt.setInt(2, appt.getServiceId());
            stmt.setTimestamp(3, new Timestamp(appt.getDate().getTime()));
            stmt.setString(4, appt.getNotes());
            stmt.setInt(5, appt.getStatus());
            stmt.setInt(6, appt.getId());

            stmt.executeUpdate(); // Ejecuta la actualización

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo básico de errores
        }
    }

    /**
     * Elimina una cita por su ID.
     *
     * @param id ID de la cita que se desea eliminar.
     */
    public void deleteAppointment(int id) {
        String sql = "DELETE FROM Appointments WHERE id = ?";

        try (
                Connection conn = ConnectionManager.getInstance().connect(); // Establece la conexión
                PreparedStatement stmt = conn.prepareStatement(sql)          // Prepara la consulta
        ) {
            stmt.setInt(1, id); // Establece el ID a eliminar
            stmt.executeUpdate(); // Ejecuta la eliminación

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo básico de errores
        }
    }
}
