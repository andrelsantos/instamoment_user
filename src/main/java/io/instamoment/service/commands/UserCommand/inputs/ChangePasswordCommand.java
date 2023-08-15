package io.instamoment.service.commands.UserCommand.inputs;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ChangePasswordCommand {
    private Long userId;

    @NotBlank(message = "A senha não pode ser vazia.")
    private String password;
    private Integer codeAccess;
}
