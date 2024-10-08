package xyz.sangdam.global.rests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import xyz.sangdam.global.Utils;
import xyz.sangdam.global.tests.TestTokenService;
import xyz.sangdam.member.constants.UserType;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ApiRequest {
    private final RestTemplate restTemplate;
    private final ObjectMapper om;
    private final Utils utils;
    private final TestTokenService tokenService;

    private boolean test;
    private UserType userType;

    private ResponseEntity<JSONData> response;
    private JSONData jsonData;

    public ApiRequest request(String url, String serviceId) {
        return request(url, serviceId, HttpMethod.GET, null);
    }

    public ApiRequest request(String url, String serviceId, HttpMethod method) {
        return request(url, serviceId, method, null);
    }

    public ApiRequest request(String url, String serviceId, HttpMethod method, Object data) {
        String requestUrl = utils.url(url, serviceId);
        method = Objects.requireNonNullElse(method, HttpMethod.GET);

        HttpHeaders headers = new HttpHeaders();

        if (System.getenv("spring.profiles.active").contains("test")) {
            test = true;
        }

        tokenService.setApiRequest(this);
        String token = test ? tokenService.getToken(Objects.requireNonNullElse(userType, UserType.STUDENT)) : utils.getToken();
        test = false;
        if (StringUtils.hasText(token)) { // 토큰이 있다면 토큰 함께 전달
            headers.setBearerAuth(token);
        }

        if (method != HttpMethod.GET && method != HttpMethod.DELETE) { // POST, PUT, PATCH 방식인 경우
            headers.setContentType(MediaType.APPLICATION_JSON);
            try {
                String body = om.writeValueAsString(data);
                HttpEntity<String> request = new HttpEntity<>(body, headers);
                System.out.println("body : " + body);
                System.out.println("request : " + request);
                this.response = restTemplate.exchange(URI.create(requestUrl), method, request, JSONData.class);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else { // GET 또는 DELETE 방식인 경우

            HttpEntity<Void> request = new HttpEntity<>(headers);
            this.response = restTemplate.exchange(URI.create(requestUrl), method, request, JSONData.class);
        }

        if (this.response != null) {
            jsonData = this.response.getBody();
        }
        return this;
    }

    /**
     * 응답 코드
     *
     * @return
     */
    public HttpStatusCode getStatus() {
        return response.getStatusCode();
    }

    public ResponseEntity<JSONData> getResponse() {
        return response;
    }

    public JSONData getData() {
        return jsonData;
    }

    /**
     * JSON으로 응답 데이터 변환
     *
     * @param clazz
     * @param <T>
     * @return
     */

    public <T> T toObj(Class<T> clazz) {

        JSONData jsonData = response.getBody();
        try {
            String body = om.writeValueAsString(jsonData.getData());
            if (StringUtils.hasText(body)) {
                return om.readValue(body, clazz);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON으로 응답 데이터 변환
     *
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> List<T> toList(TypeReference<List<T>> typeReference) {

        JSONData jsonData = response.getBody();
        try {
            String body = om.writeValueAsString(jsonData.getData());
            if (StringUtils.hasText(body)) {
                return om.readValue(body, typeReference);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 헤더 정보 조회
     *
     * @return
     */
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }

    /**
     * 문자열로 응답 데이터 조회
     *
     * @return
     */
    public String toString() {
        return (String) jsonData.getData();
    }
}