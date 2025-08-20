package fr.sd.reservcreneaux.reservcreneauxfront.Bean;

public class ProfilUtilisateur {
    private String firstName;
    private String lastName;
    private String address;
    private String description;

    // Default constructor
    public ProfilUtilisateur() {
    }

    // Parameterized constructor
    public ProfilUtilisateur(String firstName, String lastName, String address, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.description = description;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
