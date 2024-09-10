package xyz.sangdam.member.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.sangdam.member.entities.DeptInfo;

public interface DeptInfoRepository extends JpaRepository<DeptInfo, String> {
}
