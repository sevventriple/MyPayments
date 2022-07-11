package com.example.mypayments.service.impl;

import com.example.mypayments.model.Client;
import com.example.mypayments.model.Role;
import com.example.mypayments.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private ClientRepository clientRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client user = clientRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(String.format("username %s not found", username));
        return new User(user.getUsername(), user.getPassword(), mapRolesToGrantedAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToGrantedAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}
