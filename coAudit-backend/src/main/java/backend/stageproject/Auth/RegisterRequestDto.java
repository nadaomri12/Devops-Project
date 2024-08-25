package backend.stageproject.Auth;

import backend.stageproject.Entity.Role;
import lombok.Builder;

@Builder

public class RegisterRequestDto {

    private String email;
    private String fullName;
    private String password;
    private Boolean canlogin;
    private String pathAvatar ;
    private String language;
    private Role role;


    public String getLanguage() {
        return language;
    }


    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPathAvatar() {
        return pathAvatar;
    }

    public void setPathAvatar(String pathAvatar) {
        this.pathAvatar = pathAvatar;
    }

    public Boolean getCanlogin() {
        return canlogin;
    }

    public void setCanlogin(Boolean canlogin) {
        this.canlogin = canlogin;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
