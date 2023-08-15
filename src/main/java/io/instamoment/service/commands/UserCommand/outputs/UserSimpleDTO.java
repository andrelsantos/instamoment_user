package io.instamoment.service.commands.UserCommand.outputs;

import io.instamoment.service.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleDTO {
    private Long userId;
    private String name;
    private String lastName;
    private String email;

    public UserSimpleDTO(User user) {
        convert(user, user.getUserId());
    }

    public UserSimpleDTO(User user, Long userId) {
        convert(user, userId);
    }

    public void convert(User user, Long userId) {
        this.userId = userId;
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
