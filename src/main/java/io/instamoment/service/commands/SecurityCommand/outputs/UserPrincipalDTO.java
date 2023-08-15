package io.instamoment.service.commands.SecurityCommand.outputs;

import com.google.gson.Gson;

import io.instamoment.service.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.security.Principal;

@Getter
@Setter
public class UserPrincipalDTO {
    private Long userId;
    private String email;
    private String name;
    private String token;

    public UserPrincipalDTO(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.name = user.getName();
    }

    public static UserPrincipalDTO convertPrincipal(Principal principal, String token) {
        UserPrincipalDTO userPrincipalDTO = new Gson().fromJson(principal.getName(), UserPrincipalDTO.class);
        userPrincipalDTO.setToken(token);
        return userPrincipalDTO;
    }

    public static UserPrincipalDTO convertPrincipal(Principal principal) {
        UserPrincipalDTO userPrincipalDTO = new Gson().fromJson(principal.getName(), UserPrincipalDTO.class);
        return userPrincipalDTO;
    }
}
