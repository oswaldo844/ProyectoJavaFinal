package esfe.dominio;

public class Service {
    private int id;
    private String name;
    private int durationMinutes;
    private double price;
    private boolean status; // Nuevo campo

    // Constructor actualizado con status
    public Service(int id, String name, int durationMinutes, double price, boolean status) {
        this.id = id;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.status = status;
    }

    // Constructor original opcional (si lo necesitas para compatibilidad)
    public Service(int id, String name, int durationMinutes, double price) {
        this.id = id;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.status = true; // Valor por defecto: activo
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getter y setter para status
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
