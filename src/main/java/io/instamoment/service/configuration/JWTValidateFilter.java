package io.instamoment.service.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
public class JWTValidateFilter extends BasicAuthenticationFilter {
    private final JwtConfiguration jwtConfiguration;

    public JWTValidateFilter(AuthenticationManager authenticationManager,  JwtConfiguration jwtConfiguration) {
        super(authenticationManager);
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String atributo = request.getHeader(jwtConfiguration.getHeader());

        if (atributo == null) {
            chain.doFilter(request, response);
            return;
        }

        if (!atributo.startsWith(jwtConfiguration.getPrefix()+ " ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = atributo.replace(jwtConfiguration.getPrefix() + " ", "");
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        /*
        if(request.getHeader("typeUser") == null) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getOutputStream().print(new ApiExceptionHandler.Error("Missing user's currently logged typeUser", "AccessForbiddenException: Missing user's currently logged typeUser.").toString());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return;
        }

        ETypeUser typeUser;
        try{
            typeUser = ETypeUser.valueOf(request.getHeader("typeUser"));
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getOutputStream().print(new ApiExceptionHandler.Error("The provided typeUser is invalid, integer number expected.", "NumberFormatException: The provided typeUser is invalid, integer number expected.").toString());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return;
        }

        if(authenticationToken != null) {
            UserPrincipalDTO user = UserPrincipalDTO.convertPrincipal(authenticationToken.getPrincipal().toString(), token);

            Boolean boolTypeUser = userService.findTypeUser(typeUser, user.getUserId());

            if(!boolTypeUser) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getOutputStream().print(new ApiExceptionHandler.Error("The currently logged in user does not access type user.", "AccessForbiddenException: The currently logged in user does not access type user.").toString());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                return;
            }
        }
        */

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {

        String user = JWT.require(Algorithm.HMAC512(jwtConfiguration.getSecurityKey()))
                .build()
                .verify(token)
                .getSubject();

        if (user == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(user,null, new ArrayList<>());
    }
}
