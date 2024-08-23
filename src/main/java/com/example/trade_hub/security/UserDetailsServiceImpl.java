package com.example.trade_hub.security;


import com.example.trade_hub.entities.AppUser;
import com.example.trade_hub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
   private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(userRepository.findByEmail(username));
       AppUser appUser = userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found with given email"));
       return new UserDetailsImpl(appUser);
    }
}
