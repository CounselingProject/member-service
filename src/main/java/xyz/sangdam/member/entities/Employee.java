package xyz.sangdam.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import xyz.sangdam.member.constants.Gender;
import xyz.sangdam.member.constants.Status;

import java.util.UUID;

@Data
@Entity
@Table(name="EMP_INFO")
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee extends Member {
    @Column(length=10, nullable = false)
    private String userName;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="deptNo")
    private DeptInfo deptInfo; // 부서 혹은 학과

    @Column(length=10, nullable = false)
    private String empNo;

    @Column(length=50)
    private String mobile; // 휴대전화번호

    @Column(length=100)
    private String gid = UUID.randomUUID().toString();

    @Column(length=30)
    private String subject; // 담당 과목

    @Column(length=10)
    private String zonecode; // 우편번호

    @Column(length=60)
    private String address; // 주소

    @Column(length=60)
    private String addresssub; // 상세 주소

    @Column(length=10)
    private String birth; // 생년월일

    @Enumerated(EnumType.STRING)
    @Column(length=10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(length=10)
    private Status status; // 재직 상태
}
