package com.project.echoproject.service;

import com.project.echoproject.dto.UseAmountForm;

public interface UseAmountService {
    void saveUseAmount(String userId, UseAmountForm useAmountForm);
}