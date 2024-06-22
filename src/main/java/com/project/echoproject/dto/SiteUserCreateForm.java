package com.project.echoproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteUserCreateForm {

    @Size(min =3, max =25)
    @NotEmpty(message = "사용자 ID는 필수 항목입니다.")
    private String userId;

    @NotEmpty(message = "이름은 필수 항목입니다.")
    private String userName;

    @NotEmpty(message = "전화번호는 필수 항목입니다.")
    private String phoneNum;

    @NotEmpty(message = "성별은 필수 항목입니다.")
    private String gender;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email
    private String email;
}
