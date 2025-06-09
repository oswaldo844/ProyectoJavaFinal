package esfe.persistencia;

import esfe.dominio.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    // Insertar cliente
    public void insertClient(Client client) throws SQLException {
        String sql = "INSERT INTO Clients (name, phone, email) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getEmail());
            stmt.executeUpdate();
        }
    }

    // Obtener todos los clientes activos
    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients WHERE status = 1";

        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                ));
            }
        }

        return clients;
    }

    // Actualizar cliente
    public void updateClient(Client client) throws SQLException {
        String sql = "UPDATE Clients SET name = ?, phone = ?, email = ? WHERE id = ?";

        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getEmail());
            stmt.setInt(4, client.getId());
            stmt.executeUpdate();
        }
    }

    // Eliminar cliente (puedes cambiar esto a status = 0 si es eliminación lógica)
    public void deleteClient(int id) throws SQLException {
        String sql = "DELETE FROM Clients WHERE id = ?";

        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
