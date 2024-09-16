package xyz.sangdam.member.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import xyz.sangdam.member.controllers.RequestLogin;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Setter
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환을 위한 ObjectMapper

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        RequestLogin form = new RequestLogin();
        form.setEmail(request.getParameter("email"));
        form.setPassword(request.getParameter("password"));

        // 실패 이유에 따른 코드 설정
        if (exception instanceof BadCredentialsException) {
            form.setCode("BadCredentials.Login");
        } else if (exception instanceof DisabledException) {
            form.setCode("Disabled.Login");
        } else if (exception instanceof CredentialsExpiredException) {
            form.setCode("CredentialsExpired.Login");
        } else if (exception instanceof AccountExpiredException) {
            form.setCode("AccountExpired.Login");
        } else if (exception instanceof LockedException) {
            form.setCode("Locked.Login");
        } else {
            form.setCode("Fail.Login");
        }

        form.setDefaultMessage(exception.getMessage());
        form.setSuccess(false);

        // JSON으로 실패 정보 반환
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(form));
    }

    /*
    async function handleLogin() {
  try {
    const response = await fetch('/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      console.log(errorData.code); // 로그인 실패 코드
      console.log(errorData.defaultMessage); // 실패 메시지 출력
      // 실패 시 화면에 에러 메시지 표시 등 처리
    } else {
      // 로그인 성공 처리
    }
  } catch (error) {
    console.error('로그인 요청 중 오류 발생:', error);
  }
}

     */
}
