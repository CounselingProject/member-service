package xyz.sangdam.member;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import xyz.sangdam.member.entities.Employee;
import xyz.sangdam.member.entities.Member;
import xyz.sangdam.member.entities.Professor;
import xyz.sangdam.member.entities.Student;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    public boolean isLogin() {
        return getMember() != null;
    }

    public boolean isEmployee() {

        return isLogin() && getMember() instanceof Employee;
    }

    public boolean isProfessor() {
        return isLogin() && getMember() instanceof Professor;
    }

    public boolean isStudent() {

        return isLogin() && getMember() instanceof Student;
    }

    public boolean isAdmin() {
        return isLogin() && getMember() instanceof Employee;
    }

    public <T extends Member> T getMember() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo memberInfo) {

            return (T)memberInfo.getMember();
        }

        return null;
    }
}