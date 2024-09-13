package xyz.sangdam.member.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.sangdam.global.Utils;
import xyz.sangdam.global.rests.ApiRequest;
import xyz.sangdam.global.tests.TestTokenService;
import xyz.sangdam.member.constants.UserType;
import xyz.sangdam.member.repositories.MemberRepository;
import xyz.sangdam.member.services.MemberInfoService;
import xyz.sangdam.member.services.MemberSaveService;

import java.nio.charset.Charset;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
//@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    private TestTokenService tokenService;

    @Autowired
    private MemberSaveService saveService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberInfoService memberInfoService;

    private RequestJoin form;
    @Autowired
    private ApiRequest apiRequest;
    @Autowired
    private Utils utils;

    void init() {
        for (long i = 1L; i <= 10L; i++) {
            form = new RequestJoin();

            // 공통
            form.setEmail("user" + i + "@test.org");
            form.setPassword("_aA12345678");
            form.setConfirmPassword(form.getPassword());
            form.setUserName("사용자" + i);
            form.setMobile("010-1000-1000");
            form.setBirth(LocalDate.of(1990, 1, 1));
            form.setGender("MALE");
            form.setAgree(true);
            form.setUserType("STUDENT");
            form.setZonecode("123456");
            form.setAddress("어드레스");
            form.setAddressSub("어드레스서브");
            form.setStatus("EMPLOYED");
            form.setDeptNm("컴퓨터 공학과");
            form.setDeptNo("01");

            // 학생만
            form.setStdntNo("20240101");
            form.setGrade("1학년");

            System.out.println(form);
            saveService.save(form);
        }
    }
/*
    @Test
    void test1() {
        ApiRequest result = apiRequest.request("/field/list", "counseling-service", HttpMethod.GET);
        if (!result.getStatus().is2xxSuccessful()) {
            throw new BadRequestException(utils.getMessage("Fail.Field.Get"));
        }
        List<Field> fieldsList = result.toList(new TypeReference<List<Field>>() {});
//        System.out.println(fieldsList.get(0).getId());
        for (long i = 1L; i <= 10L; i++) {
            RequestJoin form = RequestJoin.builder()
                    .email("mock" + i + "@test.org")
                    .userName("mock사용자"+i)
                    .password("_aA123456")
                    .confirmPassword("_aA123456")
                    .birth(LocalDate.of(1990, 1, 1))
                    .gender(Gender.MALE.name())
                    .mobile("01012341234")
                    .job(Job.PROFESSOR.toString())
                    .memMajor(fieldsList.get(0).getId())
                    .memMinor(fieldsList.get((int) i + 10).getId())
                    .interests(List.of(fieldsList.get((int) i + 20).getId(), fieldsList.get((int) i + 30).getId(), fieldsList.get((int) i + 40).getId()))
                    .agree(true)
                    .build();
            System.out.println(form);
            saveService.save(form);
        }
    }
 */

    @Test
    @DisplayName("회원 가입 테스트")
    void joinTest() throws Exception {
        String params = om.writeValueAsString(form);

      tokenService.setApiRequest(apiRequest);
        String token =  tokenService.getToken(UserType.STUDENT);

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authentication", "Bearer " + token)
                        .characterEncoding(Charset.forName("UTF-8"))
                        .content(params))
                .andDo(print());
    }


    @Test
    void test2() {
        //apiRequest.setTest(true);
        ApiRequest result = apiRequest.request("/account", "member-service");
    }
}
