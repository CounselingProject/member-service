package xyz.sangdam.member.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.sangdam.global.Utils;
import xyz.sangdam.global.exceptions.BadRequestException;
import xyz.sangdam.global.rests.JSONData;
import xyz.sangdam.member.MemberInfo;
import xyz.sangdam.member.MemberUtil;
import xyz.sangdam.member.entities.Member;
import xyz.sangdam.member.jwt.TokenProvider;
import xyz.sangdam.member.services.MemberInfoService;
import xyz.sangdam.member.services.MemberSaveService;
import xyz.sangdam.member.validators.JoinValidator;

@Tag(name = "Member", description = "회원 API")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final JoinValidator joinValidator;
    private final MemberSaveService saveService;
    private final MemberInfoService infoService;
    private final TokenProvider tokenProvider;
    private final MemberUtil memberUtil;
    private final Utils utils;

    @Operation(summary = "인증(로그인)한 회원 정보 조회")
    @ApiResponse(responseCode = "200", description = "학생, 교수/상담자, 관리자에 따라 개인정보 조회 범위가 다르다<br>조회 가능 범위<br>학생 : 학과, 지도교수, 주소, 휴대폰 번호, 이메일<br>교수/상담사 : 담당 과목, 휴대폰 번호, 이메일")
    // 로그인한 회원 정보 조회
    @GetMapping("/account")
    @PreAuthorize("isAuthenticated()")
    public JSONData info(@AuthenticationPrincipal MemberInfo memberInfo) {
        Member member = memberInfo.getMember();

        return null;
    }

    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "201", description = "회원가입 성공시 201")
    @Parameters({
            @Parameter(name="email", required = true, description = "이메일"),
            @Parameter(name="password", required = true, description = "비밀번호"),
            @Parameter(name="confirmPassword", required = true, description = "비밀번호 확인"),
            @Parameter(name="userName", required = true, description = "사용자명"),
            @Parameter(name="mobile", description = "휴대전화번호, 형식 검증 있음"),
            @Parameter(name="agree", required = true, description = "회원가입약관 동의")
    })
    @PostMapping("/account")
    public ResponseEntity join(@RequestBody @Valid RequestJoin form, Errors errors) {

        joinValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        saveService.save(form);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "인증 및 토큰 발급", description = "인증 성공시 JWT 토큰 발급")
    @ApiResponse(responseCode = "201", headers = @Header(name="application/json"), description = "data이 발급 받은 토큰")

    @Parameters({
            @Parameter(name="email", required = true, description = "이메일"),
            @Parameter(name="password", required = true, description = "비밀번호")
    })
    @PostMapping("/account/token")
    public JSONData token(@RequestBody @Valid RequestLogin form, Errors errors) {

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        String token = tokenProvider.createToken(form.getEmail(), form.getPassword());

        return new JSONData(token);
    }
} 
