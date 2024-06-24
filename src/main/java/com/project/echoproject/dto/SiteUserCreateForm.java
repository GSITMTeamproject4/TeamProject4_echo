package com.project.echoproject.dto;

import com.project.echoproject.entity.SiteUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SiteUserCreateForm {

    @NotEmpty(message = "이름은 필수 항목입니다.")
    private String userName;

    @NotEmpty(message = "성별은 필수 항목입니다.")
    private String gender;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$", message = "아이디는 최소 8자 이상의 영어와 숫자 조합이어야 합니다.")
    @Size(min = 8,max=15, message = "아이디는 최소 8자 이상이어야 합니다." )
    @NotEmpty(message = "사용자 ID는 필수 항목입니다.")
    private String userId;


    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.",max = 15)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$", message = "비밀번호는 최소 8자 이상의 영어와 숫자 조합이어야 합니다.")
    private String password;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password2;

    @Pattern(regexp = "^\\d{11}$", message = "전화번호는 11자리 숫자여야 합니다.")
    @NotEmpty(message = "전화번호는 필수 항목입니다.")
    private String phone_num;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message="이메일 형식을 지켜주세요.")
    private String email;



    private String imgUrl;

public static SiteUserCreateForm toSiteUserCreateForm(SiteUser siteUser){
    SiteUserCreateForm siteUserCreateForm = new SiteUserCreateForm();
    siteUserCreateForm. setUserName(siteUser.getUserName());
    siteUserCreateForm. setGender(siteUser.getGender());
    siteUserCreateForm.setUserId(siteUser.getUserId());
    siteUserCreateForm.setPassword(siteUser.getPassword());
    siteUserCreateForm.setEmail(siteUser.getEmail());
    siteUserCreateForm.setPhone_num(siteUser.getPhoneNum());
    siteUserCreateForm.setImgUrl(siteUser.getImgUrl());
    return siteUserCreateForm;
}

}