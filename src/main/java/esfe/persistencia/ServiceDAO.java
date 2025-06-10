package esfe.persistencia;

import esfe.dominio.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    public void insertar(Service service) throws SQLException {
        Connection conn = ConnectionManager.getInstance().connect();
        String sql = "INSERT INTO Services (name, durationMinutes, price, status) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, service.getName());
        stmt.setInt(2, service.getDurationMinutes());
        stmt.setDouble(3, service.getPrice());
        stmt.setBoolean(4, service.isStatus()); // ← Nuevo campo
        stmt.executeUpdate();
    }

    public List<Service> obtenerTodos() throws SQLException {
        Connection conn = ConnectionManager.getInstance().connect();
        String sql = "SELECT id, name, durationMinutes, price, status FROM Services";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Service> servicios = new ArrayList<>();
        while (rs.next()) {
            servicios.add(new Service(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("durationMinutes"),
                    rs.getDouble("price"),
                    rs.getBoolean("status") // ← Se incluye status en el constructor
            ));
        }
        return servicios;
    }

    public void eliminar(int id) throws SQLException {
        Connection conn = ConnectionManager.getInstance().connect();
        String sql = "UPDATE Services SET status = 0 WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public void actualizar(Service service) throws SQLException {
        Connection conn = ConnectionManager.getInstance().connect();
        String sql = "UPDATE Services SET name = ?, durationMinutes = ?, price = ?, status = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, service.getName());
        stmt.setInt(2, service.getDurationMinutes());
        stmt.setDouble(3, service.getPrice());
        stmt.setBoolean(4, service.isStatus()); // ← Actualiza también el estado
        stmt.setInt(5, service.getId());
        stmt.executeUpdate();
    }
}
