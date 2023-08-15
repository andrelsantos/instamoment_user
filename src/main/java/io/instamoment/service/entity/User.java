package io.instamoment.service.entity;

import io.instamoment.service.commands.UserCommand.inputs.ChangeUserCommand;
import io.instamoment.service.commands.UserCommand.inputs.CreateUserCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String lastName;
    @NonNull
    private String email;
    private String password;
    private Integer codeAccess;    
    private Boolean active;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;


    public User(CreateUserCommand command) {
        this.name = command.getName();
        this.lastName = command.getLastName();
        this.email = command.getEmail();
        this.active = true;
        this.createDate = LocalDateTime.now();
    }


    public void update(ChangeUserCommand command) {
        this.name = command.getName();
        this.lastName = command.getLastName();
        this.email = command.getEmail();
        this.updateDate = LocalDateTime.now();
    }

}
