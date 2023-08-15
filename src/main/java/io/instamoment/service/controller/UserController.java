package io.instamoment.service.controller;

import io.instamoment.service.commands.UserCommand.inputs.ChangePasswordCommand;
import io.instamoment.service.commands.UserCommand.inputs.ChangeTokenFirebaseCommand;
import io.instamoment.service.commands.UserCommand.inputs.ChangeUserCommand;
import io.instamoment.service.commands.UserCommand.inputs.CreateUserCommand;
import io.instamoment.service.commands.UserCommand.inputs.ForgotMyPasswordCommand;
import io.instamoment.service.commands.UserCommand.inputs.ValidationIForgotMyPasswordCommand;
import io.instamoment.service.commands.UserCommand.outputs.UserSimpleDTO;
import io.instamoment.service.service.UserService;
import io.instamoment.service.util.ErrorMessageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Api( tags = "User")
public class UserController {

    @Autowired
    private UserService service;

    @ApiOperation(value = "", authorizations = {@Authorization(value = "Authorization")})
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserViewDTOById(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findUser(userId));
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateUserCommand command) throws MessagingException {
        return ResponseEntity.ok(service.create(command));
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "Authorization")}, response = UserSimpleDTO.class)
    @PostMapping("forgot-my-password")
    public ResponseEntity<?> iForgotMyPassword(@RequestBody ForgotMyPasswordCommand command) throws MessagingException {
        UserSimpleDTO userSimpleDTO = service.iForgotMyPassword(command.getEmail());
        return userSimpleDTO != null? ResponseEntity.ok(userSimpleDTO) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessageUtil.getUserNotFound());
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("validation-code-forgot-my-password")
    public ResponseEntity<?> validationCodeIForgotMyPassword(@RequestBody ValidationIForgotMyPasswordCommand command) {
        UserSimpleDTO userSimpleDTO = service.validationCodeIForgotMyPassword(command);
        return userSimpleDTO != null? ResponseEntity.ok(userSimpleDTO) : ResponseEntity.badRequest().body(ErrorMessageUtil.getCodeAccessInvalid());
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "Authorization")})
    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordCommand command) {
        UserSimpleDTO userSimpleDTO = service.changePassword(command);
        return userSimpleDTO != null? ResponseEntity.ok(userSimpleDTO) : ResponseEntity.badRequest().body(ErrorMessageUtil.getCodeAccessInvalid());
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "Authorization")})
    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable Long userId, @RequestBody ChangeUserCommand command) {
        service.update(userId, command);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "", authorizations = {@Authorization(value = "Authorization")}, response = UserSimpleDTO.class)
    @PutMapping("token-firebase")
    public ResponseEntity<?> changeTokenFirebaseCommand(@RequestBody ChangeTokenFirebaseCommand command) {
        service.changeTokenFirebaseCommand(command);
        return ResponseEntity.ok().build();
    }

}
