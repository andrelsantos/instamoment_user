package io.instamoment.service.commands.UserCommand.inputs;

import lombok.Getter;

@Getter
public class ChangeTokenFirebaseCommand {
    private Long userId;
    private String token;
}
