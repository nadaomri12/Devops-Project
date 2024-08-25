package backend.stageproject.Auth;

import backend.stageproject.Entity.Role;
import backend.stageproject.Entity.User;
import backend.stageproject.Repository.UserRepository;
import backend.stageproject.Security.JwtGenerater;
import backend.stageproject.Services.UserService;
import backend.stageproject.Token.Token;
import backend.stageproject.Token.TokenRepository;
import backend.stageproject.Token.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerater jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtGenerater jwtService, UserService userservice, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequestDto request) {
        // Création de l'utilisateur à partir des données reçues
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .canlogin(request.getCanlogin())
                .pathAvatar(request.getPathAvatar())
                .language(request.getLanguage())
                .role(request.getRole())

                .build();
        generateCode(user);
        User savedUser = userRepository.save(user);

        userRepository.save(savedUser);
        var jwtToken = jwtService.generateToken(savedUser);
        revokeAllUserTokens(savedUser);
        saveUserToken(savedUser,jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void generateCode(User user) {
        // Obtenez le dernier utilisateur trié par code
        User lastUser = userRepository.findTopByOrderByCodeDesc();
        String lastCode = lastUser != null ? lastUser.getCode() : "U0000";

        // Extraire les 4 derniers caractères du code
        String lastCodeNumberStr = lastCode.substring(lastCode.length() - 4);

        // Convertir en entier et incrémenter
        int lastCodeNumber = Integer.parseInt(lastCodeNumberStr);
        int newCodeNumber = lastCodeNumber + 1;

        // Générer le nouveau code
        String newCode = "CO" + "-" + "U" + String.format("%04d", newCodeNumber);
        user.setCode(newCode);
    }

    public AuthenticationResponse authenticate(AuthenticationRequestDto request) {

        //Récupérer l'utilisateur depuis la base de données
        var userr = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));


        //Authentifier l'utilisateur à l'aide de l'authenticationManager

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()

                )
        );

        // Générer un token JWT pour l'utilisateur authentifié et la retourner
        var jwtToken = jwtService.generateToken(userr);
        revokeAllUserTokens(userr);
        saveUserToken(userr, jwtToken);


        UUID userId = userr.getId();
        Role role = userr.getRole();

        // Return the response with token, userId, and role
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(userId)
                .role(role)  
                .build();
    }


    /* public void forgotpassword(String email){
         User user = userRepository.findByEmail(email)
                 .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));


     }

*/


    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


}

