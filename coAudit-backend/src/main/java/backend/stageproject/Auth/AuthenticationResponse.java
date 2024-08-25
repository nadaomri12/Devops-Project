package backend.stageproject.Auth;

import backend.stageproject.Entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthenticationResponse {

    private String token;
    public UUID userId;
    public Role role;


}