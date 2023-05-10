package com.ebcr.services;

import com.ebcr.models.User;
import com.ebcr.repositories.UserRepository;
import com.ebcr.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       try{
           User user = userRepository.findUserByUserName(s);
           if(user == null || user.getPassword() == "" || !StringUtils.hasText(user.getPassword())){
               log.error("Username provided is not found in the database.");
               throw  new UsernameNotFoundException("The provided username not found");
           }
           return UserPrincipal.create(user);
       }catch (Exception e){
           System.out.println("Can not retrieve the user: " + e.getMessage());
           return null;
       }
    }
}
