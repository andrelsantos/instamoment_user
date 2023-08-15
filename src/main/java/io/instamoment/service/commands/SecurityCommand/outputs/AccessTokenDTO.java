package io.instamoment.service.commands.SecurityCommand.outputs;

import lombok.Data;

@Data
public class AccessTokenDTO {
    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String scope;
    private String user;
    private Long userId;

    public AccessTokenDTO(String token, UserDetailsDTO userDetailsDTO, String expiresIn) {
        this.access_token = token;
        this.token_type = "bearer";
        this.expires_in = Integer.parseInt(expiresIn);
        this.scope = "all";
        this.user = userDetailsDTO.getUsername();
        this.userId = userDetailsDTO.getUserId();
    }
}
