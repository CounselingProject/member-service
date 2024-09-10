package xyz.sangdam.member.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.sangdam.member.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
