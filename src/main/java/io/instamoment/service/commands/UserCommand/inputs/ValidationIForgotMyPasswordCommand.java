package io.instamoment.service.commands.UserCommand.inputs;

import lombok.Data;

@Data
public class ValidationIForgotMyPasswordCommand {
    private Long userId;
    private Integer codeAccess;
}
