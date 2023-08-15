package io.instamoment.service.commands.UserCommand.inputs;

import lombok.Getter;

@Getter
public class ChangeUserCommand {
    private String name;
    private String lastName;
    private String cpf;
    private String phoneNumber;
    private String email;
    private String picture;
}
