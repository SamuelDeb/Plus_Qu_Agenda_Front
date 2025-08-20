package fr.sd.reservcreneaux.reservcreneauxfront.Bean;

import fr.sd.reservcreneaux.reservcreneauxfront.Dto.UtilisateurDTO;
import fr.sd.reservcreneaux.reservcreneauxfront.entities.ProfilUtilisateur;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.security.Principal;
import java.util.Map;

import java.util.logging.Logger;

@Named
@RequestScoped
public class AccueilBean implements Serializable {
//    private static final long serialVersionUID = 1L;

    private String username;
    private ProfilUtilisateur profile;
    private UtilisateurDTO user;

    @PostConstruct
    public void init() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        username = (String) sessionMap.get("username");
        if (username != null) {
            loadUserProfile();
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String reserverCreneaux() {
        return "reserverCreneaux?faces-redirect=true";
    }

    public ProfilUtilisateur getProfile() {
        return profile;
    }

    public void setProfile(ProfilUtilisateur profile) {
        this.profile = profile;
    }

    public UtilisateurDTO getUser() {
        return user;
    }

    public void setUser(UtilisateurDTO user) {
        this.user = user;
    }


    public void loadUserProfile() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/reservcreneaux/users/search/" + username);
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            user = response.readEntity(UtilisateurDTO.class);
        } else {
//            Logger.severe("Failed to load user profile: " + response.getStatus());
        }

        response.close();
        client.close();
    }
}
