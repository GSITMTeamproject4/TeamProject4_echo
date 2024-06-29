package com.project.echoproject.service;

import com.project.echoproject.dto.UseAmountForm;
import com.project.echoproject.entity.UseAmount;

import java.util.Map;

public interface UseAmountService {
    void saveUseAmount(String userId, UseAmountForm useAmountForm);
    Map<Integer, UseAmount> getMonthlyUseAmounts(String userId, int year);
}




