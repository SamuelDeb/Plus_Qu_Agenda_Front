package fr.sd.reservcreneaux.reservcreneauxfront.Bean;


import fr.sd.reservcreneaux.reservcreneauxfront.Dto.UtilisateurDTO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class AdminBean implements Serializable {

    private List<UtilisateurDTO> users;
    private Client client;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        loadUsers();
    }

    public void loadUsers() {
        Response response = client.target("http://localhost:8080/reservcreneaux/admin/protected")
                .request(MediaType.APPLICATION_JSON)
                .get();
        users = response.readEntity(new GenericType<List<UtilisateurDTO>>() {});
    }

    public void activateUser(String username) {
        client.target("http://localhost:8080/reservcreneaux/admin/activate/" + username)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(""));
        loadUsers();
    }

    public void disableUser(String username) {
        client.target("http://localhost:8080/reservcreneaux/admin/disable/" + username)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(""));
        loadUsers();
    }

    public void deleteUser(String username) {
        client.target("http://localhost:8080/reservcreneaux/admin/delete/" + username)
                .request(MediaType.APPLICATION_JSON)
                .delete();
        loadUsers();
    }

    public List<UtilisateurDTO> getUsers() {
        return users;
    }
}
