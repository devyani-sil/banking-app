package com.example.banking_app.security;

import com.example.banking_app.entity.User;
import com.example.banking_app.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
    // Used to fetch user from database
    private UserRepository userRepository;

    // Constructor Injection
    public CustomerUserDetailsService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    /*
     Spring Security automatically calls this method
     during authentication (login).

     It receives the username.
     In our application, username = email.
    */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        /*
         Search user by email.

         Example:
         SELECT * FROM users
         WHERE email='abc@gmail.com'
        */
        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));

        /*
         Convert our User entity into Spring Security's User.

         Spring Security understands only UserDetails.
        */
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                /*
                 Convert roles into authorities.

                 ROLE_USER
                 ROLE_ADMIN
                */
                user.getRoles()
                        .stream()
                        .map(role ->
                                new SimpleGrantedAuthority(
                                        /*
                                         Converts enum to String.

                                         ROLE_USER
                                         ROLE_ADMIN
                                        */
                                        role.getName().name()))
                        .toList());
    }
}
