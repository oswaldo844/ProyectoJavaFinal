package esfe.persistencia;

import esfe.dominio.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public void addAppointment(Appointment appt) {
        String sql = "INSERT INTO Appointments (client_id, service_id, date, notes, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appt.getClientId());
            stmt.setInt(2, appt.getServiceId());
            stmt.setTimestamp(3, new java.sql.Timestamp(appt.getDate().getTime()));
            stmt.setString(4, appt.getNotes());
            stmt.setInt(5, appt.getStatus());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = """
            SELECT a.id, a.client_id, a.service_id, a.date, a.notes, a.status,
                   c.name AS clientName,
                   s.name AS serviceName
            FROM Appointments a
            JOIN Clients c ON a.client_id = c.id
            JOIN Services s ON a.service_id = s.id
            """;

        try (Connection conn = ConnectionManager.getInstance().connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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

                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void updateAppointment(Appointment appt) {
        String sql = """
            UPDATE Appointments
            SET client_id = ?, service_id = ?, date = ?, notes = ?, status = ?
            WHERE id = ?
            """;

        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appt.getClientId());
            stmt.setInt(2, appt.getServiceId());
            stmt.setTimestamp(3, new Timestamp(appt.getDate().getTime()));
            stmt.setString(4, appt.getNotes());
            stmt.setInt(5, appt.getStatus());
            stmt.setInt(6, appt.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAppointment(int id) {
        String sql = "DELETE FROM Appointments WHERE id = ?";

        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
