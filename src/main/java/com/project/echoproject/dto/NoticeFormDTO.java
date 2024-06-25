package com.project.echoproject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeFormDTO {
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(min =10, max = 100)
    private String subject;

    @Size(min =10, max = 1000)
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

}
