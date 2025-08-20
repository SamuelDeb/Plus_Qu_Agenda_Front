package fr.sd.reservcreneaux.reservcreneauxfront.Bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.logging.Logger;

@Named
@RequestScoped
public class ProfileBean {

    private static final Logger LOGGER = Logger.getLogger(ProfileBean.class.getName());

    private String username;
    private ProfilUtilisateur profile = new ProfilUtilisateur();

    public void saveProfile() {
        LOGGER.info("Saving profile for user: " + username);
        LOGGER.info("First Name: " + profile.getFirstName());
        LOGGER.info("Last Name: " + profile.getLastName());
        LOGGER.info("Address: " + profile.getAddress());
        LOGGER.info("Description: " + profile.getDescription());

        Client client = ClientBuilder.newClient();
        try {
            Response response = client.target("http://localhost:8080/reservcreneaux/profiles/update")
                    .queryParam("username", username)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(profile, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Profile saved successfully!"));
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/profiles/" + username);
            } else {
                String errorMessage = response.readEntity(String.class);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", errorMessage));
                LOGGER.severe("Error saving profile: " + errorMessage);
            }
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
            LOGGER.severe("Exception saving profile: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ProfilUtilisateur getProfile() {
        return profile;
    }

    public void setProfile(ProfilUtilisateur profile) {
        this.profile = profile;
    }
}

