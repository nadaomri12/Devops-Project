package backend.stageproject.Entity;

import backend.stageproject.Repository.UserRepository;
import backend.stageproject.Token.Token;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Builder
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter


public class User extends GenericEntity implements UserDetails {

    private String fullName;
    private String email;
    private String password;
    private Boolean canlogin;
    private String pathAvatar;
    private String language;

    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonBackReference(value = "")
    @OneToMany(mappedBy = "responsible", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Operation> operationList;

    @ManyToOne
    @JoinColumn(name = "poste_id")
    private Poste poste;

    public void generateCode() {

    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Token> tokens;
}
