package xyz.sangdam.member.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.sangdam.member.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
