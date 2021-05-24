package pl.stadler.counter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.stadler.counter.models.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// verify credentials
public class JwtUserAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtUserAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // grab username and password sent by a client
        try {
            User authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), // principal
                    authenticationRequest.getPassword() // credential
            );

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), // principal
                    authenticationRequest.getPassword() // credential
            );

            // authenticationManager check if user exists and if exists it will check that the password is correct or not
            return authenticationManager.authenticate(authentication);


        } catch (IOException e) {
            throw new RuntimeException("Error");
        }
    }

    // this will be involved after attemptAuthentication will be successful
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // create token
        String token = Jwts.builder()
                .setSubject(authResult.getName()) // kto jest użytkownikiem
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())   // data stworzenia tokena
//                .setExpiration(java.sql.Date.valueOf(LocalDate(dateTime) // ustawienie czasu po jakim token ma się wygasić
                .setExpiration(new Date(System.currentTimeMillis() + 10000000))// ustawienie czasu po jakim token ma się wygasić
                .signWith(SignatureAlgorithm.HS512, "secretKey".getBytes())
                .compact();// zwraca w postaci String

        // send token to client
        System.out.println(authResult.getAuthorities());
        System.out.println(token);
        User user = new User("", null, "", "", "", token);
        String jsonString = new Gson().toJson(user);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "PATCH,POST,GET,OPTIONS,DELETE,HEAD");
        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().write(jsonString);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("Użytkownik nie występuje w bazie danych!!!");
        User user = new User("", null, "", "", "", "");
        String jsonString = new Gson().toJson(user);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonString);
    }
}
