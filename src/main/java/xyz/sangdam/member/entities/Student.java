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
    @Column(length=10)
    private String grade; // 학년

    @Column(length=10)
    private String stdntNo; // 학번
}
