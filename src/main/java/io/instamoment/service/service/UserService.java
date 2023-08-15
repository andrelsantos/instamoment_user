package io.instamoment.service.service;

import io.instamoment.service.commands.SecurityCommand.outputs.PrincipalLoginDTO;
import io.instamoment.service.commands.UserCommand.inputs.ChangePasswordCommand;
import io.instamoment.service.commands.UserCommand.inputs.ChangeTokenFirebaseCommand;
import io.instamoment.service.commands.UserCommand.inputs.ChangeUserCommand;
import io.instamoment.service.commands.UserCommand.inputs.CreateUserCommand;
import io.instamoment.service.commands.UserCommand.inputs.ValidationIForgotMyPasswordCommand;
import io.instamoment.service.commands.UserCommand.outputs.*;
import io.instamoment.service.entity.User;

import io.instamoment.service.exception.ExistingLoginException;
import io.instamoment.service.exception.UserNotFoundException;
import io.instamoment.service.repository.UserRepository;
import io.instamoment.service.util.RandomNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import java.util.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;

    //@Autowired
    //private SendEmailService sendEmailService;


    public User findById(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }

    public UserSimpleDTO findUser(Long userId) {
        return new UserSimpleDTO(this.findById(userId));
    }
    
    public UserSimpleDTO iForgotMyPassword(String username) throws MessagingException {
        Optional<User> userOptional = repository.findByEmailAndActiveTrue(username);
        if (userOptional.isPresent()) {
            userOptional.get().setCodeAccess(RandomNumberUtil.generateRandomNumberWithFiveDigits());
            repository.saveAndFlush(userOptional.get());

            //sendEmailService.sendEmailCode(userOptional.get(), ETypeEmailCode.RESEND_CODE);
            return new UserSimpleDTO(userOptional.get());
        }
        return null;
    }

    public UserSimpleDTO validationCodeIForgotMyPassword(ValidationIForgotMyPasswordCommand command) {
        Optional<User> userOptional = repository.findUserByUserIdAndCodeAccessAndActiveTrue(command.getUserId(), command.getCodeAccess());
        return userOptional.map(UserSimpleDTO::new).orElse(null);
    }

    public UserSimpleDTO changePassword(ChangePasswordCommand command) {
        Optional<User> userOptional = repository.findUserByUserIdAndCodeAccessAndActiveTrue(command.getUserId(), command.getCodeAccess());
        if (userOptional.isPresent()) {
            userOptional.get().setPassword(command.getPassword());
            userOptional.get().setCodeAccess(null);
            repository.saveAndFlush(userOptional.get());
            return new UserSimpleDTO(userOptional.get());
        }
        return null;
    }

    @Transactional
    public UserSimpleDTO create(CreateUserCommand command) throws MessagingException {
        Optional<User> userOptional = repository.findByEmailAndActiveTrue(command.getEmail());
        if (userOptional.isPresent())
            throw new ExistingLoginException(command.getEmail());

        User user = new User(command);
        user.setCodeAccess(RandomNumberUtil.generateRandomNumberWithFiveDigits());
        repository.save(user);

        //sendEmailService.sendEmailCode(user, ETypeEmailCode.CREATE_USER);
        return new UserSimpleDTO(user);
    }

	public Optional<User> loadUserLogin(String username) {
        PrincipalLoginDTO login = new Gson().fromJson(username, PrincipalLoginDTO.class);
		return repository.findByEmailAndActiveTrue(login.getEmail());
	}

    public void update(Long userId, ChangeUserCommand command) {
        User user = this.findById(userId);
        user.update(command);
        repository.save(user);
    }

    public void changeTokenFirebaseCommand(ChangeTokenFirebaseCommand command) {
        User user = this.findById(command.getUserId());
        repository.save(user);
    }

}
