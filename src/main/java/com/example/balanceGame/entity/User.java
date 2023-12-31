package com.example.balanceGame.entity;

import com.example.balanceGame.controller.http.request.ModifyRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User{
    // 기본키 생성 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_key")
    private Long userKey;

    // 아이디
    private String userId;

    // 비밀번호
    private String userPw;

    // 이름
    private String userName;

    // 이메일
    private String userEmail;

    // 계정 상태 ex) 0 현재 사용, 1 휴먼
    private boolean userStatus;

    // 회원가입 시간
    private LocalDateTime createDate;

    // 탈퇴 시간
    private LocalDateTime deleteDate;

    // 작성한 게시글
    @OneToMany(mappedBy = "user")
    private List<Board> boards;

    // 작성한 댓글
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    // 유저 정보를 수정하는 비즈니스 로직
    public void modifyUser(ModifyRequest modifyRequest) {
        this.userId = modifyRequest.getUserId();
        this.userEmail = modifyRequest.getUserEmail();
        this.userName = modifyRequest.getUserName();
    }

    // 비밀번호를 수정하는 비즈니스 로직
    public void modifyPw(String modifyPw) {
        this.userPw = modifyPw;
    }
}
