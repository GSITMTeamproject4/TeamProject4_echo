package com.project.echoproject;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Answer {
@Id    // 기본키, 각 데이터들을 구분하는 유효한 값(중복 불가능)
@GeneratedValue(strategy = GenerationType.IDENTITY) // 고유한 번호를 생성하는 방법
private Integer id;
}