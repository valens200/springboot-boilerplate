package com.ebcr.security;

import com.ebcr.enums.Gender;
import com.ebcr.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UserPrincipal  implements UserDetails {
    private UUID id;

    private String email;

    private String firstName;

    private String lastName;
    private String username;

    private String mobile;

    private Gender gender;
    private String phoneNumber;
    @JsonIgnore
    private String password;

    private Collection<? extends  GrantedAuthority> authorities;

    public UserPrincipal(UUID id, String email, String fullName, String lastName, String phoneNumber, Gender gender, String password, List<GrantedAuthority> authorities1) {
          this.id = id;
          this.email = email;
          this.firstName = fullName;
          this.lastName = lastName;
          this.phoneNumber = phoneNumber;
          this.gender = gender;
          this.password = password;
          this.authorities = authorities1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public  static UserPrincipal create(User user){
        List<GrantedAuthority>authorities1 =new ArrayList<>();
        authorities1 = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getGender(),
                user.getPassword(),
                authorities1
        );
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
