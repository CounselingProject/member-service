package xyz.sangdam.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import xyz.sangdam.global.entities.BaseEntity;
import xyz.sangdam.member.constants.UserType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="USER_INFO")
@Inheritance(strategy = InheritanceType.JOINED)
public class Member extends BaseEntity {
    @Id @GeneratedValue
    private Long seq; // 사용자 번호

    @Column(length=50, unique = true, nullable = false)
    private String email; // 로그인 ID

    @Column(length=200, nullable = false)
    private String password; // 비밀번호

    private int loginErrCnt; // 로그인 오류 횟수

    @Column(length=1)
    private String firYn; // 개정 잠금 여부

    private LocalDateTime rcntDt; // 최근 접속 일시

    @Enumerated(EnumType.STRING)
    @Column(length=10, nullable = false)
    private UserType userSe; // 사용자 구분
}