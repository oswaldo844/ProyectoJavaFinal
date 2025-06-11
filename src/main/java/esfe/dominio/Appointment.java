package esfe.dominio;

import java.util.Date;

public class Appointment {
    private int id;
    private int clientId;
    private int serviceId;
    private Date date;
    private String notes;
    private int status;

    public Appointment() {}

    public Appointment(int id, int clientId, int serviceId, Date date, String notes, int status) {
        this.id = id;
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.date = date;
        this.notes = notes;
        this.status = status;
    }
    private String clientName;
    private String serviceName;

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