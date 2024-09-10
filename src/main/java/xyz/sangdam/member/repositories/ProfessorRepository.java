package xyz.sangdam.member.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.sangdam.member.entities.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
