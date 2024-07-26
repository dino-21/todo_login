package com.example.demo.service;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//UserService는 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 새로운 사용자 엔티티를 생성합니다.
     *
     * @param userEntity 생성할 사용자 엔티티
     * @return 저장된 사용자 엔티티
     * @throws RuntimeException 유효하지 않은 인자나 사용자 이름이 이미 존재하는 경우
     */

    public UserEntity create(final UserEntity userEntity) {
        if(userEntity == null || userEntity.getUsername() == null ) {
            throw new RuntimeException("Invalid arguments");
        }
        final String username = userEntity.getUsername();
        if(userRepository.existsByUsername(username)) {
            log.warn("Username already exists {}", username);
            throw new RuntimeException("Username already exists");
        }

        return userRepository.save(userEntity);
    }

    /**
     * 주어진 사용자 이름과 비밀번호로 사용자 엔티티를 가져온다.
     *
     * @param username 사용자의 이름
     * @param password 사용자의 비밀번호
     * @param encoder 비밀번호 암호화 및 검증을 위한 PasswordEncoder 객체
     * @return 주어진 사용자 이름과 비밀번호로 인증된 사용자 엔티티, 인증 실패 시 null
     */

    public UserEntity getByCredentials(final String username, final String password, final PasswordEncoder encoder) {
        // 사용자 이름으로 사용자 정보를 조회
        final UserEntity originalUser = userRepository.findByUsername(username);

        // matches 메서드를 이용해 패스워드가 같은지 확인
        if (originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            // 패스워드가 일치하면 사용자 정보를 반환
            return originalUser;
        }

        // 사용자 정보가 없거나 패스워드가 일치하지 않으면 null 반환
        return null;
    }
}
