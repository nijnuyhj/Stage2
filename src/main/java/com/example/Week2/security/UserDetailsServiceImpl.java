package com.example.Week2.security;

import com.example.Week2.entity.Member;
import com.example.Week2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Autowired
    public UserDetailsServiceImpl(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("유저를 찾을 수 없습니다." +username));

        return new UserDetailsImpl(member);
    }
}
