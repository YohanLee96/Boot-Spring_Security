package com.study.login.domain.service;

import com.study.login.domain.model.User;
import com.study.login.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service("formUserDetailService")
public class FormUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if(user == null) {
            throw new UsernameNotFoundException("This user is unauthenticated user.");
        }

        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), user.getAuthorities());
    }
}
