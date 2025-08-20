package fr.sd.reservcreneaux.reservcreneauxfront.entities;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProfilUtilisateur {
    private String nom;
    private String prenom;
    private String adresse;
    private String description;

    // Getters and setters...

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}