package fr.sd.reservcreneaux.reservcreneauxfront.Dto;

import fr.sd.reservcreneaux.reservcreneauxfront.entities.ProfilUtilisateur;

public class UtilisateurDTO {
    private String username;
    private String email;
    private String statut;
    private ProfilUtilisateur profile;

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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public ProfilUtilisateur getProfile() {
        return profile;
    }

    public void setProfile(ProfilUtilisateur profile) {
        this.profile = profile;
    }
}

