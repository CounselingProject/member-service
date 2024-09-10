package xyz.sangdam.member.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import xyz.sangdam.member.constants.Gender;
import xyz.sangdam.member.constants.Status;
import xyz.sangdam.member.constants.UserType;

import java.time.LocalDate;

@Data
public class RequestUpdate {

    @NotBlank @Size(min=8)
    private String password;

    @NotBlank
    private String confirmPassword;

    private String userSe = UserType.STUDENT.name(); // 사용자 구분 - STUDENT가 기본값

    @NotBlank
    private String userName;

    private String mobile;

    private String zonecode; // 우편번호
    private String address; // 주소
    private String addresssub; // 상세 주소
    private String birth; // 생년월일
    private String gender = Gender.FEMALE.name(); // 성별
    private String status = Status.ONCLASS.name(); // 기본값 수업 중

    private String deptNo; // 부서번호

    // 학생 정보
    private String grade; // 학년
    private String stdntNo; // 학번

    // 교직원 정보
    private String empNo; // 사번
    private String subject; // 담당 과목

    // 교수 정보
    @JsonFormat(pattern="yyyy-mm-dd")
    private LocalDate stateDate; // 시작일자
    private LocalDate endDate; // 종료일자
    private String nowState = Status.ONCLASS.name(); // 기본값 - 수업 중
}
