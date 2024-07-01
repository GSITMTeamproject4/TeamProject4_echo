package com.project.echoproject.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SiteUserCreateForm {

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "아이디는 최소 8자 이상의 영어와 숫자 조합이어야 합니다.")
    @Size(min = 8, max = 15)
    @NotEmpty
    private String userId;

    @Nullable
    private String userName;

    @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "전화번호 형식은 010-****-**** 여야 합니다.")
    @Nullable
    private String phoneNum;

    @Nullable
    private String gender;

    @NotEmpty
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 최소 8자 이상의 영어와 숫자 조합이어야 합니다.")
    private String password1;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email
    private String email;

    @Nullable
    private MultipartFile file;

    @Nullable
    private String zipcode;

    @Nullable
    private String streetaddr;

    @Nullable
    private String detailaddr;

}
