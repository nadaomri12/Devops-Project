package backend.stageproject.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtGenerater {

    private static final String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";


    public String getUsernameFromJWT(String token) {
        return extractClaim(token, Claims::getSubject);    // Récupère nom d'utilisateur de claims form JWT token
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails

    ) {
        // Crée le Token JWT en spécifiant le sujet, la date d'émission, la date d'expiration et la signature

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // Récupère le nom d'utilisateur de l'objet d'authentification
                .setIssuedAt(new Date(System.currentTimeMillis())) // date d'emission
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 1000)) //date d'expiration
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)  //signature
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromJWT(token); // recuperer le username from token
        // Vérifie si le nom d'utilisateur extrait du token correspond au username from userdetails
        //  test  si le token n'a pas expiré ou nn
        // si ils sont vrai  les deux =>token est valide
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    // Utilise le parseur JWT token pour extraire les revendications (claims) du token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
