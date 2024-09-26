package xyz.sangdam.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="EMP_INFO")
@JsonIgnoreProperties(ignoreUnknown = true)
//@Inheritance(strategy = InheritanceType.JOINED)
public class Employee extends Member {
    @Column(length=10, nullable = false)
    private String empNo; // 사번

    @Column(length=30)
    private String subject; // 담당 과목
}
