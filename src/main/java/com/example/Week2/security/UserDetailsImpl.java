package com.example.Week2.security;

import com.example.Week2.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private final Member member;

    public UserDetailsImpl(Member member){

        this.member = member;
    }

    public Member getMember(){
        return member;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities()
//    {
//        return Collections.emptyList();
//    }
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    // 여기서 사용자의 권한을 부여해줍니다.
    // 여기서는 간단하게 "ROLE_USER" 권한만을 부여하도록 하였습니다.
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
}

    @Override
    public String getPassword() {

        return member.getPassword();
    }

    @Override
    public String getUsername(){
        return member.getUsername();
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