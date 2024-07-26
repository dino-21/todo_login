package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.security.TokenProvider;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    // Bean으로 작성해도 됨.
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 사용자 등록 엔드포인트
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {

        log.info("Registering user: {}", userDTO);

       // userDTO나 비밀번호가 null인 경우 예외 발생
        try {
            if(userDTO == null || userDTO.getPassword() == null ) {
                throw new RuntimeException("Invalid Password value.");
            }
            // 요청을 이용해 저장할 유저 만들기
            UserEntity user = UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();
            // 서비스를 이용해 리포지터리 에 유저 저장
            UserEntity registeredUser = userService.create(user);

            // 응답으로 보낼 UserDTO 객체 생성
            UserDTO responseUserDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .build();
            // 성공적으로 등록된 사용자 정보를 응답으로 반환
            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {   // 예외가 발생하면 에러 메시지를 담은 ResponseDTO 객체를 응답으로 반환
            // 유저 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴.
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 사용자 로그인 엔드포인트
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        // UserService를 이용해 사용자 인증 시도
        UserEntity user = userService.getByCredentials(
                userDTO.getUsername(),
                userDTO.getPassword(),
                passwordEncoder);

        // 인증에 성공하면 사용자 정보를 담은 UserDTO 객체를 응답으로 반환
        if(user != null) {
            //토큰 생성
            final String token = tokenProvider.create(user);
            log.info("-----token------ : " +token);
            log.info("Authenticated user ID : " + user.getId());
            final UserDTO responseUserDTO = UserDTO.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            // 인증에 실패하면 에러 메시지를 담은 ResponseDTO 객체를 응답으로 반환
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
}


