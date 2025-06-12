package esfe.persistencia; // Paquete que contiene las clases relacionadas con el acceso a datos (persistencia)

import esfe.dominio.Service; // Importa la clase Service del paquete dominio
import java.sql.*; // Importa todas las clases necesarias para trabajar con JDBC y SQL
import java.util.ArrayList; // Importa la clase ArrayList para manejar listas dinámicas
import java.util.List; // Importa la interfaz List

// Clase pública que sirve como Data Access Object (DAO) para la entidad Service
public class ServiceDAO {

    // Método para insertar un nuevo servicio en la base de datos
    public void insertar(Service service) throws SQLException {
        // Obtiene una conexión a la base de datos
        Connection conn = ConnectionManager.getInstance().connect();
        // Consulta SQL con parámetros para insertar un nuevo servicio
        String sql = "INSERT INTO Services (name, durationMinutes, price, status) VALUES (?, ?, ?, ?)";
        // Prepara la consulta para prevenir inyecciones SQL
        PreparedStatement stmt = conn.prepareStatement(sql);
        // Asigna los valores de los parámetros a partir del objeto Service recibido
        stmt.setString(1, service.getName());
        stmt.setInt(2, service.getDurationMinutes());
        stmt.setDouble(3, service.getPrice());
        stmt.setBoolean(4, service.isStatus()); // Se incluye el estado del servicio
        // Ejecuta la consulta de inserción
        stmt.executeUpdate();
    }

    // Método para obtener todos los servicios de la base de datos (activos e inactivos)
    public List<Service> obtenerTodos() throws SQLException {
        Connection conn = ConnectionManager.getInstance().connect(); // Conexión a la base de datos
        String sql = "SELECT id, name, durationMinutes, price, status FROM Services"; // Consulta SQL
        Statement stmt = conn.createStatement(); // Crea una sentencia SQL
        ResultSet rs = stmt.executeQuery(sql); // Ejecuta la consulta y obtiene el resultado

        List<Service> servicios = new ArrayList<>(); // Lista para almacenar los servicios recuperados
        while (rs.next()) {
            // Crea un nuevo objeto Service por cada fila del resultado y lo agrega a la lista
            servicios.add(new Service(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("durationMinutes"),
                    rs.getDouble("price"),
                    rs.getBoolean("status") // Se incluye el estado en el constructor
            ));
        }
        return servicios; // Devuelve la lista de servicios
    }

    // Método para eliminar un servicio de la base de datos por su ID
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Services WHERE id = ?"; // Consulta SQL para eliminar por ID
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id); // Asigna el ID al parámetro de la consulta
            stmt.executeUpdate(); // Ejecuta la eliminación
        }
    }

    // Método para actualizar los datos de un servicio existente
    public void actualizar(Service service) throws SQLException {
        Connection conn = ConnectionManager.getInstance().connect(); // Conexión
        // Consulta SQL para actualizar los datos del servicio
        String sql = "UPDATE Services SET name = ?, durationMinutes = ?, price = ?, status = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql); // Prepara la consulta
        stmt.setString(1, service.getName()); // Asigna el nombre
        stmt.setInt(2, service.getDurationMinutes()); // Asigna la duración
        stmt.setDouble(3, service.getPrice()); // Asigna el precio
        stmt.setBoolean(4, service.isStatus()); // Asigna el estado
        stmt.setInt(5, service.getId()); // Asigna el ID del servicio a actualizar
        stmt.executeUpdate(); // Ejecuta la actualización
    }

    // Método para obtener únicamente los servicios que están activos (status = 1)
    public List<Service> getAllServices() throws SQLException {
        Connection conn = ConnectionManager.getInstance().connect(); // Conexión
        // Consulta SQL que filtra los servicios activos
        String sql = "SELECT id, name, durationMinutes, price, status FROM Services WHERE status = 1";
        Statement stmt = conn.createStatement(); // Crea la sentencia
        ResultSet rs = stmt.executeQuery(sql); // Ejecuta la consulta

        List<Service> servicios = new ArrayList<>(); // Lista para los servicios activos
        while (rs.next()) {
            // Crea y agrega el servicio activo a la lista
            servicios.add(new Service(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("durationMinutes"),
                    rs.getDouble("price"),
                    rs.getBoolean("status")
            ));
        }
        return servicios; // Devuelve la lista de servicios activos
    }
}
