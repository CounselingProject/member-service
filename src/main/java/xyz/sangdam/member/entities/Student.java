package xyz.sangdam.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name="STDNT_INFO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student extends Member {
    @Column(length=10)
    private String grade; // 학년

    @Column(length=10)
    private String stdntNo; // 학번

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="professor")
    private Employee professor;
}
