package xyz.sangdam.global.tests;

import org.springframework.security.test.context.support.WithSecurityContext;
import xyz.sangdam.member.constants.Gender;
import xyz.sangdam.member.constants.Status;
import xyz.sangdam.member.constants.UserType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory= MockSecurityContextFactory.class)
public @interface MockMember {
    long seq() default 1L;
    String gid() default "testgid";
    String email() default "user01@test.org";
    String password() default "_aA123456";
    String userName() default "사용자01";
    String mobile() default "01010001000";
    UserType userType() default UserType.STUDENT;
    //LocalDate birth() default LocalDate.of(1999,2,2);
    Status status() default Status.EMPLOYED;
    String deptNm() default  "컴퓨터공학과";
    boolean agree() default true;
    String address() default "서울특별시 서대문구";
    String zonecode() default "123456";
    String deptNo() default "01";
    String stdntNo() default "20240101";
    String grade() default "1학년";
    Gender gender() default Gender.MALE;
    String addressSub() default "어드레스서브";
}
