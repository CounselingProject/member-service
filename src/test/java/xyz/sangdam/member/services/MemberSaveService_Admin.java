package xyz.sangdam.member.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.sangdam.member.constants.Status;
import xyz.sangdam.member.constants.UserType;
import xyz.sangdam.member.controllers.RequestJoin;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberSaveService_Admin {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    private List<RequestJoin> createDummystudents() {
        return IntStream.rangeClosed(31, 40) // 21부터 30까지 범위 생성
                .mapToObj(i -> createRequestJoin(
                        String.format("user%d@test.org", i),                // 이메일: user31@test.org ~ user40@test.org
                        String.format("관리자%d", i),                       // 사용자 이름: 사용자31 ~ 사용자40
                        "_aA123456",                                        // 동일한 비밀번호
                        String.format("010-1000-10%02d", i),                // 휴대전화: 010-1000-1001 ~ 010-1000-1010
                        LocalDate.of(1960 + i - 30, i - 30, i - 30),           // 생년월일: 1990-01-01 ~ 1999-12-31
                        "전산실",                               // 학과 혹은 부서
                        String.format("2024010%d", i)               // 사번
                ))
                .collect(Collectors.toList());
    }

    // RequestJoin 객체를 만드는 메서드
    private RequestJoin createRequestJoin(String email, String username, String password, String mobile, LocalDate birthDate, String department, String empNo) {
        RequestJoin form = new RequestJoin();

        // 공통
        form.setEmail(email);
        form.setPassword(password);
        form.setConfirmPassword(password);
        form.setMobile(mobile);
        form.setUserName(username);
        form.setBirth(birthDate);
        form.setStatus(String.valueOf(Status.EMPLOYED));
        form.setUserType(String.valueOf(UserType.ADMIN));
        form.setDeptNm(department);
        form.setAgree(true);
        form.setAddress("서울특별시 서대문구");
        form.setZonecode("123456");
        form.setDeptNo("999");

        // 관리자만
        form.setEmpNo(empNo);
        form.setSubject("전산실");

        return form;
    }

    // 10명의 더미 데이터를 미리 생성
    @BeforeEach
    public void init() throws Exception {
        List<RequestJoin> users = createDummystudents();

        for (RequestJoin user : users) {
            String params = om.writeValueAsString(user);
            mockMvc.perform(post("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(Charset.forName("UTF-8"))
                            .content(params))
                    .andDo(print());
        }
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void joinTest() {

        // 회원 정보가 추가되었는지 확인하는 테스트 로직 이후에 구현
    }
}
