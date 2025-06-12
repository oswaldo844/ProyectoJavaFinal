package esfe.persistencia; // Paquete donde se encuentra esta clase

import esfe.dominio.Client; // Importación de la clase Client desde el dominio

import java.sql.*;           // Importaciones para trabajar con SQL
import java.util.ArrayList; // Lista para almacenar múltiples clientes
import java.util.List;      // Interfaz de listas

// Clase que se encarga de manejar la persistencia de datos para los clientes (DAO = Data Access Object)
public class ClientDAO {

    // Método para insertar un nuevo cliente en la base de datos
    public void insertClient(Client client) throws SQLException {
        String sql = "INSERT INTO Clients (name, phone, email) VALUES (?, ?, ?)"; // Consulta SQL

        // Bloque try-with-resources para asegurar el cierre automático de los recursos
        try (Connection conn = ConnectionManager.getInstance().connect(); // Obtener conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) {       // Preparar la sentencia SQL

            // Asignar valores a los parámetros de la sentencia
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getEmail());

            stmt.executeUpdate(); // Ejecutar la consulta de inserción
        }
    }

    // Método para obtener todos los clientes activos (status = 1)
    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>(); // Lista para almacenar los clientes obtenidos
        String sql = "SELECT * FROM Clients WHERE status = 1"; // Consulta SQL que filtra por clientes activos

        // Ejecutar la consulta y recorrer los resultados
        try (Connection conn = ConnectionManager.getInstance().connect(); // Conexión a la BD
             PreparedStatement stmt = conn.prepareStatement(sql);        // Preparar sentencia
             ResultSet rs = stmt.executeQuery()) {                        // Ejecutar y obtener resultados

            // Mientras haya resultados, crear objetos Client y agregarlos a la lista
            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                ));
            }
        }

        return clients; // Retornar la lista de clientes
    }

    // Método para actualizar los datos de un cliente existente
    public void updateClient(Client client) throws SQLException {
        String sql = "UPDATE Clients SET name = ?, phone = ?, email = ? WHERE id = ?"; // Consulta SQL

        // Ejecutar la actualización con parámetros
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establecer los valores que se van a actualizar
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getEmail());
            stmt.setInt(4, client.getId());

            stmt.executeUpdate(); // Ejecutar la consulta
        }
    }

    // Método para eliminar un cliente por su ID (eliminación física)
    // Nota: Podrías cambiar esto a una eliminación lógica (por ejemplo, UPDATE status = 0)
    public void deleteClient(int id) throws SQLException {
        String sql = "DELETE FROM Clients WHERE id = ?"; // Consulta SQL de eliminación

        // Ejecutar la eliminación
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);     // Establecer el ID del cliente a eliminar
            stmt.executeUpdate();   // Ejecutar la eliminación
        }
    }
}
