package xyz.sangdam.member.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import xyz.sangdam.global.ListData;
import xyz.sangdam.global.Pagination;
import xyz.sangdam.member.MemberInfo;
import xyz.sangdam.member.constants.UserType;
import xyz.sangdam.member.controllers.MemberSearch;
import xyz.sangdam.member.entities.Employee;
import xyz.sangdam.member.entities.Member;
import xyz.sangdam.member.entities.QEmployee;
import xyz.sangdam.member.entities.QMember;
import xyz.sangdam.member.repositories.EmployeeRepository;
import xyz.sangdam.member.repositories.MemberRepository;
import xyz.sangdam.member.repositories.StudentRepository;

import java.util.Collections;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;

    private final JPAQueryFactory queryFactory;
    private final HttpServletRequest request;

    /**
     * 사용자 정보를 가져와 인증처리하는 메서드
     * - 로그인 시 호출되는 메서드
     *
     * username으로 사용자를 찾고, 그 사용자의 UserType에 따라 추가 정보를 Employee나 Student 리포지토리에서 조회
     * 사용자의 역할(권한)을 UserType을 기준으로 부여한 후, 이를 기반으로 인증 정보를 담은 MemberInfo 객체를 반환
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        UserType userType = member.getUserType();
        if (userType == UserType.COUNSELOR || userType == UserType.ADMIN || userType == UserType.PROFESSOR) {
            member = employeeRepository.findById(member.getSeq()).orElseThrow(() -> new UsernameNotFoundException(username));
        } else if (userType == UserType.STUDENT) {
            member = studentRepository.findById(member.getSeq()).orElseThrow(() -> new UsernameNotFoundException(username));
        }

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userType.name()));

        addInfo(member);

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(authorities)
                .member(member)
                .build();
    }

    /**
     * 회원 목록 조회
     *
     * @param search
     * @return
     */
    @Transactional
    public ListData<Member> getList(MemberSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit;

        /* 검색 처리 S */
        QMember member = QMember.member;
        BooleanBuilder andBuilder = new BooleanBuilder();

        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL"; // 통합검색이 기본


        if (StringUtils.hasText(skey)) {
            /**
             * sopt 검색옵션
             * ALL - (통합검색) - email, userName
             * email - 이메일로 검색
             * userName - 닉네임으로 검색
             * mobile - 핸드폰번호로 검색
             * deptNm - 부서명 + 학과명으로 검색
             * deptNo - 부서번호로 검색
             */
            sopt = sopt.trim();
            skey = skey.trim();
            StringExpression expression = null;

            if (sopt.equals("ALL")) { // 통합 검색
                expression = member.email.concat(member.userName)
                        .concat(member.mobile)
                        .concat(member.deptNm)
                        .concat(member.deptNo);


            } else if (sopt.equals("email")) { // 이메일로 검색
                expression = member.email;

            }
            else if (sopt.equals("userName")) { // 회원명로 검색
                expression = member.userName;

            } else if (sopt.equals("mobile")) { // 핸드폰번호로 검색
                expression = member.mobile;

            } else if (sopt.equals("deptNm")) { // 부서명 + 학과명으로 검색
                expression = member.deptNm;

            } else if (sopt.equals("deptNo")) { // 부서번호로 검색
                expression = member.deptNo;
            }

            if (expression != null) andBuilder.and(expression.contains(skey));
        }

        List<String> userType = search.getUserType();
        if (userType != null && !userType.isEmpty()) {
            List<UserType> userTypes = userType.stream().map(UserType::valueOf).toList();
            andBuilder.and(member.userType.in(userTypes));
        }

        /* 검색 처리 E */

        List<Member> items = queryFactory.selectFrom(member)
                .where(andBuilder)
                .offset(offset)
                .limit(limit)
                .orderBy(member.createdAt.desc())
                .fetch();

        long total = memberRepository.count(andBuilder);
        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        return new ListData<>(items, pagination);
    }

    public void addInfo(Member member) {
        /*
        List<FileInfo> files = fileInfoService.getList(member.getGid());
        if (files != null && !files.isEmpty()) {
            member.setProfileImage(files.get(0));
        }
         */
    }

    /**
     * 지도 교수 배정 api
     * 검색어가 들어오면 지도 교수 배정
     *
     * @return
     */
    public List<Employee> getProfessors(String key) {
        if (!StringUtils.hasText(key)) {
            return Collections.EMPTY_LIST; // 해당 키워드 검색해서 매칭이 되면 검색해서 지도교수 학과명이 나오는 것으로
        }

        BooleanBuilder builder = new BooleanBuilder();
        QEmployee employee = QEmployee.employee;
        builder.and(employee.userType.eq(UserType.PROFESSOR));
        builder.and(employee.userName.concat(employee.deptNm).contains(key.trim())); // 학과외에 필요한 거 있으면 여기 추가하면 됨

        List<Employee> items = (List<Employee>) employeeRepository.findAll(builder, Sort.by(asc("userName"))); // 정렬순서는 이름순

        return items;
    }

    /**
     *  상담사 키워드 조회
     *
     * @param key
     * @return
     */
    public List<Employee> getCounselors(String key) {

        BooleanBuilder builder = new BooleanBuilder();
        QEmployee employee = QEmployee.employee;
        builder.and(employee.userType.eq(UserType.COUNSELOR));
        if (StringUtils.hasText(key)) {
            builder.and(employee.userName.concat(employee.deptNm).contains(key.trim())); // 학과외에 필요한 거 있으면 여기 추가하면 됨
        }
        List<Employee> items = (List<Employee>) employeeRepository.findAll(builder, Sort.by(asc("userName"))); // 정렬순서는 이름순

        return items;
    }

    /**
     * 랜덤하게 상담사 배치하기
     *
     * @return
     */
    public Employee getCounselor() {
        BooleanBuilder builder = new BooleanBuilder();
        QEmployee employee = QEmployee.employee;
        builder.and(employee.userType.eq(UserType.COUNSELOR));

        List<Employee> employees = (List<Employee>)employeeRepository.findAll(builder);

        if (employees != null && !employees.isEmpty()) {
            Collections.shuffle(employees);
            return employees.get(0);
        }

        return null;
    }
}
