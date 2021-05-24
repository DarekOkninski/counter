package pl.stadler.counter.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // pobranie typu autoryzacji
        String header = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationByToken(header);
        System.out.println(authenticationToken);
        // SecurityContextHolder przechowuje informacje na temat zalogowanego uzytkownika
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // teraz filter musi przekazać informacje o zalogowanym użytkowniku do naszej aplikacji
        chain.doFilter(request, response);
    }

    // to się wykonuje jako pierwsze
    private UsernamePasswordAuthenticationToken getAuthenticationByToken(String header) {
        System.out.println("header: " + header);

        if (header != null) {
            try {
                Jws<Claims> claimsJws = Jwts.parser().setSigningKey("secretKey".getBytes())
                        .parseClaimsJws(header.replace("Bearer ", ""));
                String username = claimsJws.getBody().get("sub").toString();

                List<Map<String, String>> authorities = (List<Map<String, String>>) claimsJws.getBody().get("authorities");
                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                        .map(m ->new SimpleGrantedAuthority(m.get("authority")))
                        .collect(Collectors.toSet());

                return new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);

            } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
                System.out.println("Nagłówek żadania jest pusty.");
                throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
            } catch (ExpiredJwtException ex) {
                return null;
            }
        }
        return null;
    }


}
