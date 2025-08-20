package fr.sd.reservcreneaux.reservcreneauxfront.Dto;

public class CreneauxDTO {
    private String id;
    private String date; // Utiliser String pour simplifier le transfert JSON
    private String heureDebut;
    private String heureFin;
    private String type;
    private String creeParUsername;
    private String reserverParUsername;
    private String statut = "disponible";
    private String description;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getCreeParUsername() {
        return creeParUsername;
    }

    public void setCreeParUsername(String creeParUsername) {
        this.creeParUsername = creeParUsername;
    }

    public String getReserverParUsername() {
        return reserverParUsername;
    }

    public void setReserverParUsername(String reserverParUsername) {
        this.reserverParUsername = reserverParUsername;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
