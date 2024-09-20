package com.gccloud.nocportal.Security;

import com.gccloud.nocportal.Entity.UsersSpring;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CustomUserDetails implements UserDetails {

    private UsersSpring userCred;

    public CustomUserDetails(UsersSpring userCred) {
        this.userCred = userCred;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String role = userCred.getRole();
        List<SimpleGrantedAuthority> authorityList = new ArrayList<SimpleGrantedAuthority>();
        authorityList.add(new SimpleGrantedAuthority(role));
        return authorityList;
    }

    @Override
    public String getPassword() {
        if (userCred != null) {
            return userCred.getPassword();
        } else {

            return "/error/unauthenticated";
        }
    }

    @Override
    public String getUsername() {
        return userCred.getUsername();
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
