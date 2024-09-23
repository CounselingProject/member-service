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
import org.springframework.web.bind.annotation.*;
import xyz.sangdam.global.Utils;
import xyz.sangdam.global.exceptions.BadRequestException;
import xyz.sangdam.global.rests.JSONData;
import xyz.sangdam.member.MemberInfo;
import xyz.sangdam.member.MemberUtil;
import xyz.sangdam.member.entities.Employee;
import xyz.sangdam.member.entities.Member;
import xyz.sangdam.member.jwt.TokenProvider;
import xyz.sangdam.member.services.MemberInfoService;
import xyz.sangdam.member.services.MemberSaveService;
import xyz.sangdam.member.validators.JoinValidator;
import xyz.sangdam.member.validators.UpdateValidator;

import java.util.List;

@Tag(name = "Member", description = "회원 API")
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class MemberController {

    private final JoinValidator joinValidator;
    private final MemberSaveService saveService;
    private final MemberInfoService infoService;
    private final UpdateValidator updateValidator;
    private final TokenProvider tokenProvider;
    private final MemberUtil memberUtil;
    private final Utils utils;
    private final MemberInfoService memberInfoService;

    @Operation(summary = "인증(로그인)한 회원 정보 조회")
    @ApiResponse(responseCode = "200", description = "학생, 교수/상담자, 관리자에 따라 개인정보 조회 범위가 다르다<br>조회 가능 범위<br>학생 : 학과, 지도교수, 주소, 휴대폰 번호, 이메일<br>교수/상담사 : 담당 과목, 휴대폰 번호, 이메일")
    // 로그인한 회원 정보 조회
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public JSONData info(@AuthenticationPrincipal MemberInfo memberInfo) {
        Member member = memberInfo.getMember();

        return new JSONData(member);
    }

    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "201", description = "회원가입 성공시 201")
    @Parameters({
            @Parameter(name="email", required = true, description = "이메일"),
            @Parameter(name="password", required = true, description = "비밀번호"),
            @Parameter(name="confirmPassword", required = true, description = "비밀번호 확인"),
            @Parameter(name="userName", required = true, description = "사용자명"),
            @Parameter(name="mobile", description = "휴대전화번호, 형식 검증 있음"),
            @Parameter(name="agree", required = true, description = "회원가입약관 동의"),
            @Parameter(name="userType", required = true, description = "회원 유형", example = "STUDENT, PROFESSOR, COUNSELLOR, ADMIN"),
            @Parameter(name="zonecode", required = true, description = "우편번호"),
            @Parameter(name="address", required = true, description = "주소", example = "서울시 ㅇㅇ구 ㅇㅇ동"),
            @Parameter(name="addresssub", required = false, description = "상세주소", example = "123동 345호"),
            @Parameter(name="birth", required = true, description = "생년월일"),
            @Parameter(name="gender", required = true, description = "성별"),
            @Parameter(name="status", required = true, description = "재직, 휴직, 퇴사 상태"),
            @Parameter(name="deptNm", required = true, description = "부서명이자 학과명"),
            @Parameter(name="deptNo", required = true, description = "부서번호 이자 학과번호"),
            @Parameter(name="stdntNo", required = false, description = "학번"),
            @Parameter(name="grade", required = false, description = "학년"),
            @Parameter(name="professor", required = false, description = "교수 회원 번호"),
            @Parameter(name="empNo", required = false, description = "사번"),
            @Parameter(name="subject", required = false, description = "담당 과목")
    })
    @PostMapping
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
    @PostMapping("/token")
    public JSONData token(@RequestBody @Valid RequestLogin form, Errors errors) {

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        String token = tokenProvider.createToken(form.getEmail(), form.getPassword());

        return new JSONData(token);
    }

    @Operation(summary = "회원정보 수정", method = "PATCH")
    @ApiResponse(responseCode = "200",  description = "로그인 한 회원 정보 수정")
    @Parameters({
            @Parameter(name = "email", required = true, description = "변경할 회원의 email(아이디로 사용되므로 변경 불가)", example = "user01@test.org"),
            @Parameter(name = "userName", required = true, description = "회원명", example = "사용자01"),
            @Parameter(name = "password", description = "변경할 비밀번호, 필수는 아니므로 변경 값이 넘어오면 변경 처리함", example = "_aA123456"),
            @Parameter(name = "confirmPassword", description = "password 값이 있다면 확인은 필수항목"),
            @Parameter(name = "mobile", description = "휴대전화번호"),
            @Parameter(name="userType", description = "회원 유형", example = "STUDENT, PROFESSOR, COUNSELLOR, ADMIN"),
            @Parameter(name="zonecode", description = "우편번호"),
            @Parameter(name="address", description = "주소", example = "서울시 ㅇㅇ구 ㅇㅇ동"),
            @Parameter(name="addressSub", description = "상세주소", example = "123동 345호"),
            @Parameter(name="birth", description = "생년월일"),
            @Parameter(name="gender", description = "성별"),
            @Parameter(name="status", description = "재직, 휴직, 퇴사 상태"),
            @Parameter(name="deptNm", description = "부서명이자 학과명"),
            @Parameter(name="deptNo", description = "부서번호 이자 학과번호"),
            @Parameter(name="stdntNo", description = "학번"),
            @Parameter(name="grade", description = "학년"),
            @Parameter(name="professor", description = "교수 회원 번호"),
            @Parameter(name="empNo", description = "사번"),
            @Parameter(name="subject", description = "담당 과목")
    })
    @PatchMapping("/update")
    public JSONData update(@Valid @RequestBody RequestUpdate form, Errors errors) {

        updateValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        Member member = saveService.save(form);

        return new JSONData(member);
    }

    @Operation(summary = "상담원 랜덤 조회")
    @GetMapping("/counselor")
    public JSONData randomCounselor() {
        Employee employee = memberInfoService.getCounselor();

        return new JSONData(employee);
    }

    @Operation(summary = "교수목록 키워드 검색")
    @GetMapping("/professors") // 회원가입 할 때 주로 필요하니까
    public JSONData professors(@RequestParam(name = "skey", required = false) String skey) {
        List<Employee> items = memberInfoService.getProfessors(skey);

        return new JSONData(items);
    }
} 
