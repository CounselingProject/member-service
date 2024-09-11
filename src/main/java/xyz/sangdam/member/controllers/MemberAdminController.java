package xyz.sangdam.member.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import xyz.sangdam.global.ListData;
import xyz.sangdam.global.Utils;
import xyz.sangdam.global.exceptions.BadRequestException;
import xyz.sangdam.global.rests.JSONData;
import xyz.sangdam.member.MemberInfo;
import xyz.sangdam.member.entities.Member;
import xyz.sangdam.member.services.MemberDeleteService;
import xyz.sangdam.member.services.MemberInfoService;
import xyz.sangdam.member.services.MemberSaveService;
import xyz.sangdam.member.validators.UpdateValidator;

@Tag(name="MemberAdmin", description = "회원 관리 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MemberAdminController {

    private final MemberInfoService memberInfoService;
    private final MemberSaveService memberSaveService;
    private final UpdateValidator updateValidator;
    private final MemberDeleteService memberDeleteService;
    private final Utils utils;


    @Operation(summary = "회원 목록 조회", description = "items - 조회된 회원목록, pagination - 페이징 기초 데이터", method = "GET")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="page", description = "페이지 번호", example = "1"),
            @Parameter(name="limit", description = "한페이지당 레코드 갯수", example = "20"),
            @Parameter(name="sopt", description = "검색옵션", example = "ALL"),
            @Parameter(name="skey", description = "검색키워드"),
    })
    @GetMapping
    public JSONData list(@ModelAttribute MemberSearch search) {

        ListData data = memberInfoService.getList(search);

        return new JSONData(data);
    }


    @Operation(summary = "회원 한명 조회", method = "GET")
    @ApiResponse(responseCode = "200")
    @Parameter(name="email", required = true, description = "경로변수, 회원 이메일(로그인시 아이디로 활용)")
    @GetMapping("/info/{email}")
    public JSONData info(@PathVariable("email") String email) {

        MemberInfo memberInfo = (MemberInfo)memberInfoService.loadUserByUsername(email);
        Member member = memberInfo.getMember();

        return new JSONData(member);
    }

    @Operation(summary = "회원정보 수정", method = "PATCH")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="email", required = true, description = "변경할 회원의 email(아이디로 사용되므로 변경 불가)", example="user01@test.org"),
            @Parameter(name="userName", required = true, description = "회원명", example = "사용자01"),
            @Parameter(name="password", description = "변경할 비밀번호, 필수는 아니므로 변경 값이 넘어오면 변경 처리함", example = "_aA123456"),
            @Parameter(name="confirmPassword", description = "password 값이 있다면 확인은 필수항목"),
            @Parameter(name="mobile", description = "휴대전화번호"),
            @Parameter(name="authority", description = "변경할 권한 목록, 필수는 아니므로 값이 있을 때만 변경 처리, 다중 권한 지원하므로 여러 권한을 배열 형태로 전송", example = "authority=USER&authority=MANAGER")
    })
    @PatchMapping("/update")
    public void update(@RequestBody @Valid RequestUpdate form, Errors errors) {
        updateValidator.validate(form, errors);
        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }
    }

    @Operation(summary = "회원삭제", method = "Delete")
    @ApiResponse(responseCode = "204")
    @Parameters({
            @Parameter(name="seq", description = "경로변수, 회원번호", example = "1"),
    })
    @DeleteMapping("/delete/{seq}")
    public JSONData deelte(@PathVariable("seq") Long seq) {
        Member member = memberDeleteService.deleteMember(seq);
        return new JSONData(member);
    }
}
