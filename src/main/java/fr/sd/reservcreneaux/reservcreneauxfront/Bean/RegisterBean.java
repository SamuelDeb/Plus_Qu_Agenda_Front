package fr.sd.reservcreneaux.reservcreneauxfront.Bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.primefaces.PrimeFaces;

@Named
@RequestScoped
public class RegisterBean {
    @JsonProperty
    private String username;
    @JsonProperty
    private String email;
    @JsonProperty
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void register() {
        Client client = ClientBuilder.newClient();
        Response response = null;
        try {
            response = client.target("http://localhost:8080/reservcreneaux/auth/register")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(this, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                // Si l'inscription réussit
                PrimeFaces.current().executeScript("PF('modalInscription').show();");
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
    }
}
