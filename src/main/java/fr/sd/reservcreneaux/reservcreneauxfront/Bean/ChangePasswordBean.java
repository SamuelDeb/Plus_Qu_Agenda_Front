package fr.sd.reservcreneaux.reservcreneauxfront.Bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class ChangePasswordBean {
    private static final Logger logger = Logger.getLogger(ChangePasswordBean.class.getName());

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String token;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void changePassword() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (!newPassword.equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", null));
            return;
        }

        Client client = ClientBuilder.newClient();
        String changePasswordPayload = "{\"oldPassword\":\"" + oldPassword + "\",\"newPassword\":\"" + newPassword + "\"}";

        // Log the token and payload for debugging purposes
        logger.info("Token: " + token);
        logger.info("Payload: " + changePasswordPayload);

        Response response = client.target("http://localhost:8080/reservcreneaux/users/change-password")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .post(Entity.json(changePasswordPayload));

        // Log the response status and body for debugging purposes
        logger.info("Response Status: " + response.getStatus());
        logger.info("Response Body: " + response.readEntity(String.class));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully", null));
        } else {
            logger.log(Level.SEVERE, "Change password failed: " + response.getStatus());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Change password failed", null));
        }
    }
}
