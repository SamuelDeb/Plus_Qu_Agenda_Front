package fr.sd.reservcreneaux.reservcreneauxfront.Bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sd.reservcreneaux.reservcreneauxfront.Dto.ResetPasswordRequest;
import fr.sd.reservcreneaux.reservcreneauxfront.Dto.ResetRequest;
import fr.sd.reservcreneaux.reservcreneauxfront.util.JwtParserUtil;
import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.primefaces.PrimeFaces;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class LoginBean {
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;
    private boolean loggedIn;
    private List<Slot> upcomingSlots;
    private List<Slot> reservedSlots;
    private String verificationCode;
    private String token;
    private String forgotUsername;
    private String resetCode;
    private String newPassword;
    private String confirmPassword;

    private static final Logger logger = Logger.getLogger(LoginBean.class.getName());

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public List<Slot> getUpcomingSlots() {
        return upcomingSlots;
    }

    public void setUpcomingSlots(List<Slot> upcomingSlots) {
        this.upcomingSlots = upcomingSlots;
    }

    public List<Slot> getReservedSlots() {
        return reservedSlots;
    }

    public void setReservedSlots(List<Slot> reservedSlots) {
        this.reservedSlots = reservedSlots;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getForgotUsername() {
        return forgotUsername;
    }

    public void setForgotUsername(String forgotUsername) {
        this.forgotUsername = forgotUsername;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void login() {
        Client client = ClientBuilder.newClient();
        String loginPayload = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        Response response = client.target("http://localhost:8080/reservcreneaux/auth/login")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(loginPayload));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            PrimeFaces.current().executeScript("PF('dlgVerifyCode').show()");
        } else {
            // Handle login error
            System.out.println("Login failed: " + response.getStatus());
            logger.log(Level.SEVERE, "Login failed: " + response.getStatus());

        }
    }
    public void validateCode() {
        Client client = ClientBuilder.newClient();
        String codeValidationPayload = "{\"username\":\"" + username + "\",\"code\":\"" + verificationCode + "\"}";

        Response response = client.target("http://localhost:8080/reservcreneaux/auth/validate-code")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(codeValidationPayload));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            try {
                // Lire le JSON et extraire le token
                Map<String, String> responseMap = new ObjectMapper().readValue(response.readEntity(String.class), Map.class);
                token = responseMap.get("token");
                logger.log(Level.INFO, "Token: " + token);

                // Récupérer les claims à partir du token
                Claims claims = JwtParserUtil.parseJWT(token);
                logger.log(Level.INFO, "Claims: " + claims);

                // Essayer de récupérer le username à partir du claim "sub"
                String extractedUsername = claims.getSubject(); // "sub" claim
                if (extractedUsername == null) {
                    // Si "sub" est null, essayer "upn"
                    extractedUsername = claims.get("upn", String.class);
                }
                logger.log(Level.INFO, "Extracted Username: " + extractedUsername);

                // Stocker le token et le username dans la session
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("token", token);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", extractedUsername);

                FacesContext.getCurrentInstance().getExternalContext().redirect("accueil.xhtml");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error processing validation response", e);
            }
        } else {
            // Handle code validation error
            logger.log(Level.SEVERE, "Code validation failed: " + response.getStatus());
        }

    }
    public boolean isAdmin() {
        return FacesContext.getCurrentInstance().getExternalContext().isUserInRole("admin");
    }

    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        loggedIn = false;
        return "connexion?faces-redirect=true";
    }

public void sendResetCode() {
    Client client = ClientBuilder.newClient();
    ResetRequest resetRequest = new ResetRequest();
    resetRequest.setUsername(forgotUsername);

    Response response = client.target("http://localhost:8080/reservcreneaux/auth/send-reset-code")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(resetRequest));

    if (response.getStatus() == Response.Status.OK.getStatusCode()) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Code de réinitialisation envoyé", null));
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("resetPassword.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        FacesContext.getCurrentInstance().addMessage("forgotUsername", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Utilisateur non trouvé", null));
    }
}
    public void resetPassword() {
        Client client = ClientBuilder.newClient();
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setUsername(forgotUsername);
        resetPasswordRequest.setResetCode(resetCode);
        resetPasswordRequest.setNewPassword(newPassword);
        resetPasswordRequest.setConfirmPassword(confirmPassword);

        Response response = client.target("http://localhost:8080/reservcreneaux/auth/reset-password")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(resetPasswordRequest));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mot de passe modifié avec succès", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Code invalide ou les mots de passe ne correspondent pas", null));
        }
    }

    public String editProfile() {
        // Logique pour modifier le profil
        return "profil?faces-redirect=true";
    }

    public String changePassword() {
        // Logique pour changer le mot de passe
        return "changePassword?faces-redirect=true";
    }

    public String createSlot() {
        // Logique pour créer un créneau
        return "createCreneaux?faces-redirect=true";
    }

    public class Slot {
        private String date;
        private String description;

        // Getters and setters
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}



