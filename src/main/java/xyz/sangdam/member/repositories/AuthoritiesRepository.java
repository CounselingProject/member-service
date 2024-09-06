package xyz.sangdam.member.repositories;

import xyz.sangdam.member.entities.Authorities;
import xyz.sangdam.member.entities.AuthoritiesId;
import xyz.sangdam.member.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities, AuthoritiesId>, QuerydslPredicateExecutor<Authorities> {

    List<Authorities> findByMember(Member member);
}