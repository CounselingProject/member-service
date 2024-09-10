package xyz.sangdam.member.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.sangdam.member.constants.DeptType;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeptInfo {
    @Id
    @Column(length=10)
    private String deptNo; // 부서번호

    @Column(length=50)
    private String deptNm; // 부서명

    @Enumerated(EnumType.STRING)
    @Column(length=10)
    private DeptType deptSe; // 부서구분

    @Column(length=1)
    private String useYn; // 사용여부
}
