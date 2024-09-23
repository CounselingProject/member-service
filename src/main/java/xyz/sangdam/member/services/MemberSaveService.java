package xyz.sangdam.member.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import xyz.sangdam.member.MemberInfo;
import xyz.sangdam.member.MemberUtil;
import xyz.sangdam.member.constants.Gender;
import xyz.sangdam.member.constants.Status;
import xyz.sangdam.member.constants.UserType;
import xyz.sangdam.member.controllers.RequestJoin;
import xyz.sangdam.member.controllers.RequestUpdate;
import xyz.sangdam.member.entities.Employee;
import xyz.sangdam.member.entities.Member;
import xyz.sangdam.member.entities.Student;
import xyz.sangdam.member.exceptions.MemberNotFoundException;
import xyz.sangdam.member.repositories.EmployeeRepository;
import xyz.sangdam.member.repositories.MemberRepository;
import xyz.sangdam.member.repositories.StudentRepository;



@Service
@Transactional
@RequiredArgsConstructor
public class MemberSaveService {
    private final MemberRepository memberRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentRepository studentRepository;
    private final MemberInfoService infoService;

    private final PasswordEncoder passwordEncoder;
    private final MemberUtil memberUtil;
    /**
     * 회원 가입 처리
     *
     * @param form
     */
    public void save(RequestJoin form) {
        UserType userType = StringUtils.hasText(form.getUserType()) ? UserType.valueOf(form.getUserType()) : UserType.STUDENT;

        Member member = null;
        if (userType == UserType.PROFESSOR || userType == UserType.COUNSELOR || userType == UserType.ADMIN) {
            member = new Employee();
        } else { // 학생
            member = new Student();
        }

        /* 공통 항목 처리 S */
        String hash = passwordEncoder.encode(form.getPassword()); // BCrypt 해시화
        String mobile = form.getMobile();
        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
        }
        member.setEmail(form.getEmail());
        member.setUserName(form.getUserName());
        member.setPassword(hash);
        member.setMobile(mobile);
        member.setBirth(form.getBirth());
        member.setUserType(userType);
        member.setZonecode(form.getZonecode());
        member.setAddress(form.getAddress());
        member.setAddresssub(form.getAddresssub());
        member.setGid(form.getGid());
        member.setStatus(form.getStatus() == null ? Status.EMPLOYED : Status.valueOf(form.getStatus()));
        member.setGender(form.getGender() == null ? Gender.FEMALE : Gender.valueOf(form.getGender()));
        member.setDeptNm(form.getDeptNm());
        member.setDeptNo(form.getDeptNo());
        /* 공통 항목 처리 E */

        // 상담사, 교수, 관리자 추가 정보
        if (member instanceof Employee employee) {
            employee.setEmpNo(form.getEmpNo());
            employee.setSubject(form.getSubject());

            employeeRepository.saveAndFlush(employee);

        } else if (member instanceof Student student){ // 학생 추가 정보
            student.setStdntNo(form.getStdntNo());
            student.setGrade(form.getGrade());

            studentRepository.saveAndFlush(student);
        }
    }

    /**
     * 회원정보 수정
     *
     * @param form
     */
    public Member save(RequestUpdate form) {
        Member member = memberUtil.getMember();
        String email = member.getEmail();
        member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        UserType userType = member.getUserType();

        if (userType == UserType.STUDENT) {
            member = studentRepository.findById(member.getSeq()).orElseThrow(MemberNotFoundException::new);
        } else {
            member = employeeRepository.findById(member.getSeq()).orElseThrow(MemberNotFoundException::new);
        }

        /* 공통 수정 항목 S */
        String mobile = form.getMobile();
        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
        }

        member.setMobile(mobile);
        member.setZonecode(form.getZonecode());
        member.setAddress(form.getAddress());
        member.setAddresssub(form.getAddresssub());
        member.setBirth(form.getBirth());

        String password = form.getPassword();
        if (StringUtils.hasText(password)) {
            String hash = passwordEncoder.encode(password);
            member.setPassword(hash);
        }
        /* 공통 수정 항목 E */

        // 교직원(상담사, 교수), 관리자
        if (member instanceof Employee && StringUtils.hasText(form.getStatus()) || memberUtil.isAdmin()) {
            member.setStatus(Status.valueOf(form.getStatus()));
        }


        /* 관리자인 경우만 수정 가능 항목 */
        if (memberUtil.isAdmin()) {
            member.setUserName(form.getUserName());
            member.setDeptNo(form.getDeptNo());
            member.setDeptNm(form.getDeptNm());
            member.setGender(form.getGender() == null ? null : Gender.valueOf(form.getGender()));

            if (member instanceof Student student) {
                student.setGrade(form.getGrade());
                student.setStdntNo(form.getStdntNo());
            } else if (member instanceof Employee employee) {
                employee.setEmpNo(form.getEmpNo());
                employee.setSubject(form.getSubject());
            }
        }

        if (member instanceof Employee employee) {
            employeeRepository.saveAndFlush(employee);
        } else if (member instanceof Student student) {
            studentRepository.saveAndFlush(student);
        }

        MemberInfo memberInfo = (MemberInfo) infoService.loadUserByUsername(email);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return member;
    }
}
