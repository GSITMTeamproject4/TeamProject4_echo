package com.project.echoproject.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteUserCreateForm {
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,15}$", message = "아이디는 최소 5자 이상의 영어와 숫자의 조합이어야 합니다.")
    @Size(min =5, message = "아이디는 최소 5자 이상이어야 합니다." ,max = 15)
    @NotEmpty(message = "사용자 ID는 필수 항목입니다.")
    private String userId;

    @NotEmpty(message = "이름은 필수 항목입니다.")
    private String userName;
    @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "전화번호 형식은 010-****-**** 여야 합니다.")
    @NotEmpty(message = "전화번호는 필수 항목입니다.")
    private String phoneNum;

    @NotEmpty(message = "성별은 필수 항목입니다.")
    private String gender;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.",max = 15)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 최소 8자 이상의 영어와 숫자 조합이어야 합니다.")
    private String password1;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email
    private String email;


    @Pattern(regexp = ".*\\.(png|jpg)$", message = "파일은 PNG 또는 JPG 형식이어야 합니다.")
    private String imgUrl=null;
    @NotEmpty
    private String address;


}