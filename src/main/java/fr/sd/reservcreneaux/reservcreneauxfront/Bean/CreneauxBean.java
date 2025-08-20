package fr.sd.reservcreneaux.reservcreneauxfront.Bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.primefaces.PrimeFaces;

import java.io.Serializable;

@Named
@RequestScoped
public class SlotBean implements Serializable {
    private String description;
    private int duration;
    private String startTime;
    private String endTime;
    private int days;
    private String slotType;
    private String username;

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String createSlot() {
        Client client = ClientBuilder.newClient();
        Response response = null;
        try {
            response = client.target("http://localhost:8080/users/creneaux/generate")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(this, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                // Si l'inscription réussit
                PrimeFaces.current().executeScript("PF('modalCreneauxCree').show();");
            } else {
                // Gérer les erreurs ou afficher un message approprié
                PrimeFaces.current().ajax().update("form:messages"); // Mettez à jour la partie du formulaire avec les messages d'erreur
            }

        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }

        return "accueil?faces-redirect=true";
    }
}