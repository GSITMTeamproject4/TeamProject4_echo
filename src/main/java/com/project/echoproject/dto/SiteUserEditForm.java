package com.project.echoproject.dto;

import com.project.echoproject.valid.ValidPhoneNumber;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SiteUserEditForm {
    private String userId;

    @NotEmpty(message = "이름은 필수 항목입니다.")
    private String userName;

    private String nickName;

    @Nullable
    @ValidPhoneNumber
    private String phoneNum;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private String gender;

    @Nullable
    private MultipartFile profileImage;  // 이 부분을 추가

    private String zipcode;

    private String streetaddr;

    private String detailaddr;
}

