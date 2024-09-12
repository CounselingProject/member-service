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
import org.springframework.transaction.annotation.Transactional;
import xyz.sangdam.member.constants.Status;
import xyz.sangdam.member.constants.UserType;
import xyz.sangdam.member.controllers.RequestJoin;
import xyz.sangdam.member.repositories.EmployeeRepository;
import xyz.sangdam.member.repositories.StudentRepository;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberSaveServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MemberSaveService saveService;

    private RequestJoin form;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private List<RequestJoin> createDummystudents() {
        return IntStream.rangeClosed(1, 10) // 1부터 10까지 범위 생성
                .mapToObj(i -> createRequestJoin(
                        String.format("user%d@test.org", i),                // 이메일: user1@test.org ~ user10@test.org
                        String.format("사용자%d", i),                       // 사용자 이름: 사용자1 ~ 사용자10
                        "_aA123456",                                        // 동일한 비밀번호
                        String.format("010-1000-10%02d", i),                // 휴대전화: 010-1000-1001 ~ 010-1000-1010
                        LocalDate.of(1990 + i, i, i),           // 생년월일: 1990-01-01 ~ 1999-12-31
                        "컴퓨터공학과",
                        "1학년"
                ))
                .collect(Collectors.toList());
    }

    // RequestJoin 객체를 만드는 메서드
    private RequestJoin createRequestJoin(String email, String username, String password, String mobile, LocalDate birthDate, String department, String grade) {
        RequestJoin form = new RequestJoin();

        form.setEmail(email);
        form.setPassword(password);
        form.setConfirmPassword(password);
        form.setMobile(mobile);
        form.setUserName(username);
        form.setBirth(birthDate);
        form.setStatus(String.valueOf(Status.EMPLOYED));
        form.setStdntNo(String.valueOf((int) (Math.random() * 100000))); // 최대 5자리로 제한
        form.setUserType(String.valueOf(UserType.STUDENT));
        form.setDeptNm(department);
        form.setGrade(grade);
        form.setAgree(true);
        form.setAddress("서울특별시 서대문구");
        form.setZonecode("123456");
        form.setDeptNo("01");

        return form;
    }

    // 10명의 더미 데이터를 미리 생성
    @BeforeEach
    @Transactional
    public void init() throws Exception {
        List<RequestJoin> users = createDummystudents();

        for (RequestJoin user : users) {
            String params = om.writeValueAsString(user);
            mockMvc.perform(post("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(Charset.forName("UTF-8"))
                            .content(params))
                    .andExpect(status().isOk())  // 성공 상태 코드 확인
                    .andDo(print());
        }
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void joinTest() {

        // 회원 정보가 추가되었는지 확인하는 테스트 로직 이후에 구현
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void joinTest2() throws Exception {
        RequestJoin form = new RequestJoin();
        form.setStdntNo("20240101");
        form.setEmail("user100@test.org");
        form.setPassword("_aA123456");
        form.setConfirmPassword(form.getPassword());
        form.setMobile("010-1000-1000");
        form.setUserName("사용자100");
        form.setBirth(LocalDate.of(1999, 12, 31));
        form.setStatus(String.valueOf(Status.EMPLOYED));
        form.setUserType(String.valueOf(UserType.STUDENT));
        form.setGrade("1");
        form.setDeptNm("컴퓨터공학");
        form.setZonecode("22047");
        form.setAddress("서울특별시 서대문구");
        form.setAddressSub("이화로 502번길");
        form.setAgree(true);

        String params = om.writeValueAsString(form);

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(Charset.forName("UTF-8"))
                        .content(params))
                .andExpect(status().isOk())  // 성공 상태 코드 확인
                .andDo(print());
    }

}
