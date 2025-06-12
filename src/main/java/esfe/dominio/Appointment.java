package esfe.dominio;

import java.util.Date;

/**
 * Clase que representa una cita (Appointment) en el sistema.
 * Contiene información relacionada a un cliente, servicio, fecha, notas y estado de la cita.
 */
public class Appointment {
    // Atributos básicos de la cita
    private int id;             // Identificador único de la cita
    private int clientId;       // ID del cliente relacionado a la cita
    private int serviceId;      // ID del servicio solicitado
    private Date date;          // Fecha programada de la cita
    private String notes;       // Notas adicionales sobre la cita
    private int status;         // Estado de la cita (por ejemplo: 0 = pendiente, 1 = completada, etc.)

    /**
     * Constructor vacío por defecto.
     */
    public Appointment() {}

    /**
     * Constructor con parámetros para inicializar todos los atributos principales.
     *
     * @param id         ID de la cita
     * @param clientId   ID del cliente
     * @param serviceId  ID del servicio
     * @param date       Fecha de la cita
     * @param notes      Notas adicionales
     * @param status     Estado de la cita
     */
    public Appointment(int id, int clientId, int serviceId, Date date, String notes, int status) {
        this.id = id;
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.date = date;
        this.notes = notes;
        this.status = status;
    }

    // Atributos adicionales para mostrar el nombre del cliente y del servicio
    private String clientName;   // Nombre del cliente (opcional, para mostrar en la interfaz)
    private String serviceName;  // Nombre del servicio (opcional, para mostrar en la interfaz)

    // Métodos getters y setters para cada atributo

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
}
