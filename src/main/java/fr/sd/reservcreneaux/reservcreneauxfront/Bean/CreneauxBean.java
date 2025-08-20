package fr.sd.reservcreneaux.reservcreneauxfront.Bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sd.reservcreneaux.reservcreneauxfront.Dto.CreneauxDTO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class CreneauxBean implements Serializable {

    @JsonProperty
    private String username;
    @JsonProperty
    private String selectedUsername;
    @JsonProperty
    private int dureeMinutes;
    @JsonProperty
    private int nombreJours;
    @JsonProperty
    private String heureDebut;
    @JsonProperty
    private String heureFin;
    @JsonProperty
    private String type;
    @JsonProperty
    private String description;
    private List<CreneauxDTO> creneauxList; // Créneaux créés par l'utilisateur
    private List<CreneauxDTO> reservedCreneauxList; // Créneaux réservés par l'utilisateur
    private List<CreneauxDTO> allCreneauxList; // Tous les créneaux
    private List<String> allUsernames; // Liste de tous les utilisateurs
    private List<CreneauxDTO> availableCreneauxList;//tout les creneaux disponibles


    @PostConstruct
    public void init() {
        loadCreneaux();
        loadReservedCreneaux();
        loadAllCreneaux();
        loadAllUsernames();
        loadAllCreneauxAvailable();
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(int dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public int getNombreJours() {
        return nombreJours;
    }

    public void setNombreJours(int nombreJours) {
        this.nombreJours = nombreJours;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void generate() {
        Client client = ClientBuilder.newClient();
        try {
            Response response = client.target("http://localhost:8080/reservcreneaux/creneaux/generate")
                    .queryParam("username", username)
                    .queryParam("dureeMinutes", dureeMinutes)
                    .queryParam("nombreJours", nombreJours)
                    .queryParam("heureDebut", heureDebut)
                    .queryParam("heureFin", heureFin)
                    .queryParam("type", type)
                    .queryParam("description", description)
                    .request()
                    .post(Entity.json(null));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Liste générée avec succès"));

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors de la génération", response.readEntity(String.class)));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors de la génération", e.getMessage()));
        } finally {
            client.close();
        }
    }

    public List<CreneauxDTO> getCreneauxList() {
        return creneauxList;
    }
    public List<CreneauxDTO> getReservedCreneauxList() {
        return reservedCreneauxList;
    }

    public List<CreneauxDTO> getAllCreneauxList() {
        return allCreneauxList;
    }

    public List<String> getAllUsernames() {
        return allUsernames;
    }

    public void setAllUsernames(List<String> allUsernames) {
        this.allUsernames = allUsernames;
    }

    public List<CreneauxDTO> getAvailableCreneauxList() {
        return availableCreneauxList;
    }

    public void setAvailableCreneauxList(List<CreneauxDTO> availableCreneauxList) {
        this.availableCreneauxList = availableCreneauxList;
    }

    private static final Logger logger = Logger.getLogger(CreneauxBean.class.getName());

    public void loadCreneaux() {

        Client client = ClientBuilder.newClient();
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");

        creneauxList = client.target("http://localhost:8080/reservcreneaux/creneaux/user")
                .queryParam("username", username)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<CreneauxDTO>>() {});

        logger.log(Level.INFO, "Creneaux loaded: " + creneauxList.size());
    }
    public void loadAllUsernames() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:8080/reservcreneaux/users")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            allUsernames = response.readEntity(new GenericType<List<String>>() {});
            logger.log(Level.INFO, "All usernames loaded: " + allUsernames.size());
        } else {
            logger.log(Level.SEVERE, "Failed to load all usernames: " + response.getStatus());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors du chargement des utilisateurs", null));
        }

        client.close();
    }

    public void filterCreneauxByUsername() {
        if (selectedUsername != null && !selectedUsername.isEmpty()) {
            Client client = ClientBuilder.newClient();
            Response response = client.target("http://localhost:8080/reservcreneaux/creneaux/user/all")
                    .queryParam("username", selectedUsername)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                creneauxList = response.readEntity(new GenericType<List<CreneauxDTO>>() {});
                logger.log(Level.INFO, "Filtered creneaux loaded: " + creneauxList.size());
            } else {
                logger.log(Level.SEVERE, "Failed to filter creneaux: " + response.getStatus());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors du filtrage des créneaux", null));
            }

            client.close();
        } else {
            loadAllCreneaux(); // Reload all creneaux if no username is selected
        }
    }

    public void loadReservedCreneaux() {
        Client client = ClientBuilder.newClient();
        String sessionUsername = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");

        if (sessionUsername != null) {
            Response response = client.target("http://localhost:8080/reservcreneaux/creneaux/reserver/user")
                    .queryParam("username", sessionUsername)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                reservedCreneauxList = response.readEntity(new GenericType<List<CreneauxDTO>>() {});
                logger.log(Level.INFO, "Reserved creneaux loaded: " + reservedCreneauxList.size());
            } else {
                logger.log(Level.SEVERE, "Failed to load reserved creneaux: " + response.getStatus());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors du chargement des créneaux réservés", null));
            }
        } else {
            logger.log(Level.SEVERE, "Username not found in session");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nom d'utilisateur introuvable en session", null));
        }

        client.close();
    }

    public void loadAllCreneaux() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:8080/reservcreneaux/creneaux/allCreneaux")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            allCreneauxList = response.readEntity(new GenericType<List<CreneauxDTO>>() {});
            logger.log(Level.INFO, "All creneaux loaded: " + allCreneauxList.size());
        } else {
            logger.log(Level.SEVERE, "Failed to load all creneaux: " + response.getStatus());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors du chargement de tous les créneaux", null));
        }

        client.close();
    }

    public void reserveCreneau(String creneauId) {
        Client client = ClientBuilder.newClient();
        String sessionUsername = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");

        Response response = client.target("http://localhost:8080/reservcreneaux/creneaux/reserve")
                .queryParam("creneauId", creneauId)
                .queryParam("reserverParUsername", sessionUsername)
                .request()
                .post(Entity.json(null));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Créneau réservé avec succès"));
            loadAllCreneaux(); // Refresh the list after reservation
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors de la réservation", response.readEntity(String.class)));
        }

        client.close();
    }
    public void loadAllCreneauxAvailable() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:8080/reservcreneaux/creneaux/allCreneaux")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<CreneauxDTO> allCreneaux = response.readEntity(new GenericType<List<CreneauxDTO>>() {});
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            availableCreneauxList = allCreneaux.stream()
                    .filter(creneau -> "disponible".equals(creneau.getStatut()))
                    .filter(creneau -> LocalDate.parse(creneau.getDate(), formatter).isAfter(today) || LocalDate.parse(creneau.getDate(), formatter).isEqual(today))
                    .collect(Collectors.toList());

            logger.log(Level.INFO, "Available creneaux loaded: " + availableCreneauxList.size());
        } else {
            logger.log(Level.SEVERE, "Failed to load available creneaux: " + response.getStatus());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors du chargement des créneaux disponibles", null));
        }

        client.close();
    }
}