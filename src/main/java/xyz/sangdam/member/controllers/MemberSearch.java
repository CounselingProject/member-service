package xyz.sangdam.member.controllers;

import lombok.Data;
import xyz.sangdam.global.CommonSearch;

@Data
public class MemberSearch extends CommonSearch {
    /**
     * sopt 검색옵션
     * ALL - (통합검색) - email, userName
     * email - 이메일로 검색
     * userName - 닉네임으로 검색
     * userType - 학생, 교수, 상담사 구분
     */
    private String email;
    private String userName;
    private String userType;
}
