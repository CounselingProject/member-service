package xyz.sangdam.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;
import xyz.sangdam.member.constants.Status;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name="STDNT_EMP_CN")
public class Professor extends Employee {
    private LocalDate startDate;
    private LocalDate endDate;
    private Status nowState;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "professors", fetch = FetchType.LAZY)
    private List<Student> students;
}
