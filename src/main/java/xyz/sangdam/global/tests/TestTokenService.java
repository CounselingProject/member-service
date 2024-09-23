package xyz.sangdam.global.tests;

import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import xyz.sangdam.global.rests.ApiRequest;
import xyz.sangdam.member.constants.Gender;
import xyz.sangdam.member.constants.Status;
import xyz.sangdam.member.constants.UserType;

import java.util.HashMap;
import java.util.Map;


@Service
@Setter
public class TestTokenService {
    private ApiRequest apiRequest;


    public String getToken(UserType userType) {
        Map<String, Object> params = new HashMap<>();

        // 공통
        String email = "testuser_" + userType.name() + "" + System.currentTimeMillis()+ "@testuser.org";
        String password = "_aA123456";
        params.put("email", email);
        params.put("password", password);
        params.put("confirmPassword", password);
        params.put("userName", "testuser_" + userType.name());
        params.put("mobile", "01010001000");
        params.put("birth", "1999-12-31");
        params.put("gender", Gender.FEMALE.name());
        params.put("agree", "true");
        params.put("userType", userType.STUDENT.name());
        params.put("zonecode", "123456");
        params.put("address", "어드레스");
        params.put("addresssub", "어드레스 서브");
        params.put("status", Status.EMPLOYED.name());
        params.put("deptNm", "컴퓨터 공학과");
        params.put("deptNo", "01");

        // 학생만
        params.put("stdntNo", "20240101");
        params.put("grade", "1학년");

        ApiRequest result = apiRequest.request("/account", "member-service", HttpMethod.POST, params);
        if (result.getStatus().is2xxSuccessful()) {
            // 토큰 발급
            Map<String, String> loginParams = new HashMap<>();
            loginParams.put("email", email);
            loginParams.put("password", password);

            ApiRequest result2 = apiRequest.request("/account/token", "member-service", HttpMethod.POST, loginParams);
            if (result2.getStatus().is2xxSuccessful() && result2.getData().isSuccess()) {
                return result2.toObj(String.class);
            }
        }

        return null;
    }
}
