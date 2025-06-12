package esfe.dominio; // Paquete al que pertenece esta clase

// Clase que representa a un Cliente
public class Client {
    // Atributos privados de la clase
    private int id;            // Identificador único del cliente
    private String name;       // Nombre del cliente
    private String phone;      // Teléfono del cliente
    private String email;      // Correo electrónico del cliente

    // Constructor que inicializa los atributos del cliente
    public Client(int id, String name, String phone, String email) {
        this.id = id;             // Asignación del ID
        this.name = name;         // Asignación del nombre
        this.phone = phone;       // Asignación del teléfono
        this.email = email;       // Asignación del correo electrónico
    }

    // Métodos 'getter' para acceder a los atributos privados

    // Devuelve el nombre del cliente
    public String getName() {
        return name;
    }

    // Devuelve el teléfono del cliente
    public String getPhone() {
        return phone;
    }

    // Devuelve el correo electrónico del cliente
    public String getEmail() {
        return email;
    }

    // Devuelve el ID del cliente
    public int getId() {
        return id;
    }

    // Sobrescribe el método toString para devolver el nombre del cliente
    // Esto es útil, por ejemplo, cuando se muestra el cliente en un JComboBox o JList
    @Override
    public String toString() {
        return name;
    }
}
