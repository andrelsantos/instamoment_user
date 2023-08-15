package io.instamoment.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.instamoment.service.commands.SecurityCommand.outputs.AccessTokenDTO;
import io.instamoment.service.commands.SecurityCommand.outputs.PrincipalLoginDTO;
import io.instamoment.service.commands.SecurityCommand.outputs.UserDetailsDTO;
import io.instamoment.service.configuration.JwtConfiguration;
import io.instamoment.service.entity.User;
import io.instamoment.service.exception.ApiExceptionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration) {
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    //user.getEmail(),
                    new Gson().toJson(new PrincipalLoginDTO(user.getEmail())),
                    user.getPassword(),
                    new ArrayList<>()
            ));

        } catch (IOException e) {
            throw new RuntimeException("Falha ao autenticar usuário", e);
        }

    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        String json;
        if (failed.getMessage().equals("U404")) {
            ApiExceptionHandler.Error error = new ApiExceptionHandler.Error("Usuário não encontrado!", "", failed.getMessage());
            json = new Gson().toJson(error);
            response.setStatus(404);
        } else if (failed.getMessage().equals("U401")) {
            ApiExceptionHandler.Error error = new ApiExceptionHandler.Error("Usuário não localizado, favor contate o administrador do sistema.", "", failed.getMessage());
            json = new Gson().toJson(error);
            response.setStatus(401);
        } else {
            ApiExceptionHandler.Error error = new ApiExceptionHandler.Error("Login ou senha inválida!", "", "P403");
            json = new Gson().toJson(error);
            response.setStatus(403);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authResult.getPrincipal();
        ArrayList<String> authorities = new ArrayList<>();
        authorities.add(userDetailsDTO.getUserId().toString());

        String token = JWT.create()
                .withSubject(userDetailsDTO.getUsername())
                .withClaim("userId", userDetailsDTO.getUserId())
                //.withClaim("role", userDetailsDTO.getRole())
                .withClaim("user", userDetailsDTO.getUsername())
                //.withClaim("nameInitials", StringUtil.getNameInitials(userDetailsDTO.getName()))
                //.withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC512(jwtConfiguration.getSecurityKey()));

        String json = new Gson().toJson(new AccessTokenDTO(token, userDetailsDTO, jwtConfiguration.getExpiresIn()));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}















