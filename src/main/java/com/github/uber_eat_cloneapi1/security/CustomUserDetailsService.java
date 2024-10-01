package com.github.uber_eat_cloneapi1.security;

import com.github.uber_eat_cloneapi1.models.RoleModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepo.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("email not found"));

        return new User(user.getEmail(),user.getPassword(),mapRolesToGrantedAuthorities(user.getRoles()));
    }


    private Collection<GrantedAuthority> mapRolesToGrantedAuthorities(List<RoleModel> roles) {
        return roles.stream().map(
                role->new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toList());
    }
}
