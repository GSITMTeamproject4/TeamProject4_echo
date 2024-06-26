package com.project.echoproject.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
public class ChangePasswordForm {

    @NotEmpty(message = "현재 비밀번호는 필수 입력 항목입니다.")
    private String currentPassword;

    @NotEmpty(message = "새 비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.",max = 15)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 최소 8자 이상의 영어와 숫자 조합이어야 합니다.")
    private String newPassword;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String newPasswordConfirm;
}

