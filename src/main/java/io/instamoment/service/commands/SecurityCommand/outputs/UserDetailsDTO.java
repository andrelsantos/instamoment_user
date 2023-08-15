package io.instamoment.service.commands.SecurityCommand.outputs;

import com.google.gson.Gson;

import io.instamoment.service.entity.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserDetailsDTO implements UserDetails {

    private Optional<User> user;

    public UserDetailsDTO(Optional<User> usuario) {
        this.user = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.orElse(new User()).getUserId().toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.orElse(new User()).getPassword();
    }

    @Override
    public String getUsername() {
        return new Gson().toJson(new UserPrincipalDTO(user.orElse(new User())));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return user.orElse(new User()).getActive().equals(true);
    }

    public Long getUserId() {
        return user.orElse(new User()).getUserId();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.orElse(new User()).getActive().equals(true);
    }
}
