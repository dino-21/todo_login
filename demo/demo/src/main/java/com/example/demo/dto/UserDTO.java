package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//UserDTO는 사용자 데이터 전송 객체로, 클라이언트와 서버 간의 데이터 교환을 위한 객체

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String token;  //사용자 인증을 위한 토큰
    private String username;  //사용자의 이름
    private String password;   //사용자의 비밀번호
    private String id;   //사용자의 고유 ID
}
