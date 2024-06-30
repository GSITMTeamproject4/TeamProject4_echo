package com.project.echoproject;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @NotEmpty(message = "사용자 ID는 필수 항목입니다.")
    private String userid;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호를 재확인해주세요.")
    private String password2;

    @NotEmpty(message = "유저이름은 필수 항목입니다.")
    private String username;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email
    private String email;

    @NotEmpty(message ="헨드폰 번호는 필수 항목입니다.")
    private String phone_num;

    @NotEmpty(message="성별은 필수 항목입니다.")
    private String gender;


}
