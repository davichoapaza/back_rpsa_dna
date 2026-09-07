package bo.gob.dgac.rbs.dna.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component // <-- OBLIGATORIO
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Imprimir en consola para confirmar que el filtro intercepta la ruta
        System.out.println(">>> EJECUTANDO FILTRO JWT PARA: " + request.getRequestURI());

        String token = obtenerJwtDeLaPeticion(request);

        if (StringUtils.hasText(token) && tokenProvider.validarToken(token)) {
            String username = tokenProvider.getUsernameFromToken(token);

            UserDetails userDetails = new User(username, "", Collections.emptyList());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // ESTA LÍNEA ES LA QUE EVITA QUE CAIGA EN AnonymousAuthenticationFilter
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String obtenerJwtDeLaPeticion(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}