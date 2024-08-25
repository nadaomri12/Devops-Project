package backend.stageproject.Security;

import backend.stageproject.Token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtGenerater jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(JwtGenerater jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // si l' authHeader n'est pas vide et commence par bearer il va geter  le JWT from request (authHeader)
        // getJWTFromRequest a partir de la position 7
        jwt = authHeader.substring(7);
        username = jwtService.getUsernameFromJWT(jwt);

        var isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        //it user not null and no anthenticate
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // get usename from database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            //double authentication
            // il va tester si le token n'est pas expirè et n'est pa s revoked
            //s'il oui le token est valide sinon invalide

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        return "/api/auth/authenticate".equals(path);
    }
}
