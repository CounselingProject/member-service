package xyz.sangdam.global.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.sangdam.member.constants.UserType;

@SpringBootTest
public class TestTokenServiceTest {
    @Autowired
    private TestTokenService tokenService;

    @Test
    void test1() {
        String token = tokenService.getToken(UserType.STUDENT);
        System.out.println(token);
    }
}
