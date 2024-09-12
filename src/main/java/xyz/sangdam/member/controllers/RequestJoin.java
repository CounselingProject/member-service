package xyz.sangdam.member.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import xyz.sangdam.member.constants.Gender;

import java.time.LocalDate;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestJoin {

    /* 회원공통 S */
    private String gid = UUID.randomUUID().toString();

    @NotBlank
    @Email
    private String email;

    @NotBlank @Size(min=8)
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String userName;

    @NotBlank
    private String mobile;

    @NotBlank
    private String userType; // 회원 유형 - STUDENT, PROFESSOR, COUNSELLOR, ADMIN

    @NotBlank
    private String zonecode; // 우편번호

    @NotBlank
    private String address; // 주소
    private String addressSub; // 상세 주소

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birth; // 생년월일

    @NotBlank
    private String gender = Gender.FEMALE.name(); // 성별

    @NotBlank
    private String status; // 재직, 휴직, 퇴사

    @NotBlank
    private String deptNm; // 부서명이자 학과명

    @NotBlank
    private String deptNo; // 부서번호 이자 학과번호

    /* 회원공통 E */

    // 학생 정보
    private String stdntNo; // 학번
    private String grade; // 학년

    // 교직원 정보
    private String empNo; // 사번
    private String subject; // 담당 과목

    @AssertTrue
    private boolean agree;
}