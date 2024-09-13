package xyz.sangdam.member.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import xyz.sangdam.member.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, QuerydslPredicateExecutor<Employee> {
}
