package com.ferdev.cafe.Security;

import com.ferdev.cafe.Entities.User;
import com.ferdev.cafe.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private User userEntity;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userEntity= userRepository.findByEmailId(username);

        if (!Objects.isNull(userEntity))
            return new org.springframework.security.core.userdetails.User(
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    true,true,true,true,
                    new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not found");
    }

    public User getUserDetail() {
        return userEntity;
    }

}

