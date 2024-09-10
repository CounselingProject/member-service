package xyz.sangdam.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import xyz.sangdam.global.entities.BaseEntity;
import xyz.sangdam.member.constants.Gender;
import xyz.sangdam.member.constants.Status;
import xyz.sangdam.member.constants.UserType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    @Column(length=10, nullable = false)
    private UserType userType; // 사용자 구분

    @Column(length=10, nullable = false)
    private String userName; // 성명

    @Column(length=50)
    private String mobile; // 핸드폰 번호

    @Column(length=100)
    private String gid = UUID.randomUUID().toString(); // 파일

    @Column(length=10)
    private String zonecode; // 우편번호

    @Column(length=60)
    private String address; // 주소

    @Column(length=60)
    private String addresssub; // 상세주소

    @Column(length=10)
    private LocalDate birth; // 생년월일

    @Enumerated(EnumType.STRING)
    @Column(length=10)
    private Gender gender; // 성별

    @Enumerated(EnumType.STRING)
    @Column(length=10)
    private Status status; // 재직 상태

    @Column(length=50)
    private String deptNm; // 부서명 이자 학과명

    @Id
    @Column(length=10)
    private String deptNo; // 부서번호


    /*
    private int loginErrCnt; // 로그인 오류 횟수

    @Column(length=1)
    private String firYn; // 개정 잠금 여부

    private LocalDateTime rcntDt; // 최근 접속 일시
     */
}