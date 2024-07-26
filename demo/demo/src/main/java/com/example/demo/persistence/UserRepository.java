package com.example.demo.persistence;


import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity, String> {
    UserEntity findByUsername(String username); // 주어진 username 사용자 이름으로 UserEntity를 찾는다.
    Boolean existsByUsername(String username); //  사용자 이름이 존재하는지 확인
    // 주어진 사용자 이름과 비밀번호로 UserEntity를 찾는다.
    UserEntity findByUsernameAndPassword(String username, String password);
}
