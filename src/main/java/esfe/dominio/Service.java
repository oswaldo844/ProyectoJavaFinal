package esfe.dominio; // Paquete al que pertenece esta clase, puede usarse para organizar mejor el proyecto

// Clase pública que representa un servicio (como un corte de cabello, lavado, etc.)
public class Service {
    private int id; // Identificador único del servicio
    private String name; // Nombre del servicio
    private int durationMinutes; // Duración del servicio en minutos
    private double price; // Precio del servicio
    private boolean status; // Estado del servicio: true = activo, false = inactivo

    // Constructor que recibe todos los atributos, incluyendo el estado (status)
    public Service(int id, String name, int durationMinutes, double price, boolean status) {
        this.id = id; // Asigna el id recibido al atributo de la clase
        this.name = name; // Asigna el nombre recibido
        this.durationMinutes = durationMinutes; // Asigna la duración
        this.price = price; // Asigna el precio
        this.status = status; // Asigna el estado (activo o inactivo)
    }

    // Constructor alternativo que no recibe el estado, lo establece como true (activo) por defecto
    public Service(int id, String name, int durationMinutes, double price) {
        this.id = id; // Asigna el id
        this.name = name; // Asigna el nombre
        this.durationMinutes = durationMinutes; // Asigna la duración
        this.price = price; // Asigna el precio
        this.status = true; // Por defecto, el servicio se considera activo
    }

    // Métodos getter y setter para acceder y modificar cada uno de los atributos

    public int getId() {
        return id; // Devuelve el id del servicio
    }

    public void setId(int id) {
        this.id = id; // Establece el id del servicio
    }

    public String getName() {
        return name; // Devuelve el nombre del servicio
    }

    public void setName(String name) {
        this.name = name; // Establece el nombre del servicio
    }

    public int getDurationMinutes() {
        return durationMinutes; // Devuelve la duración en minutos
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes; // Establece la duración
    }

    public double getPrice() {
        return price; // Devuelve el precio
    }

    public void setPrice(double price) {
        this.price = price; // Establece el precio
    }

    public boolean isStatus() {
        return status; // Devuelve el estado del servicio (true = activo)
    }

    public void setStatus(boolean status) {
        this.status = status; // Establece el estado del servicio
    }

    // Sobrescribe el método toString para que el objeto muestre su nombre al imprimirse
    @Override
    public String toString() {
        return name; // Al imprimir un objeto Service, se mostrará solo su nombre
    }
}
