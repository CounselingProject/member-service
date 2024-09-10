package xyz.sangdam.member.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.sangdam.member.constants.Gender;
import xyz.sangdam.member.constants.Status;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="STDNT_INFO")
public class Student extends Member {
    @Column(length=10, nullable = false)
    private String userName; // 성명

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="deptNo")
    private DeptInfo deptInfo;

    @Column(length=10)
    private String grade; // 학년

    @Column(length=10)
    private String stdntNo; // 학번

    @Column(length=50)
    private String mobile;

    @Column(length=100)
    private String gid = UUID.randomUUID().toString();

    @Column(length=10)
    private String zonecode; // 우편번호

    @Column(length=60)
    private String address; // 주소

    @Column(length=60)
    private String addresssub; // 상세주소

    @Column(length=10)
    private String birth; // 생년월일

    @Enumerated(EnumType.STRING)
    @Column(length=10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(length=10)
    private Status status;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Professor> professors;
}
