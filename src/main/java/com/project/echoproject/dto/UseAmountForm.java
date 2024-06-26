package com.project.echoproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class UseAmountForm {
    private LocalDate useDate=LocalDate.now();
    private long useElectricity;
    private long useGas;
}