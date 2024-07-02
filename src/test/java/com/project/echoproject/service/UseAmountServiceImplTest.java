package com.project.echoproject.service;

import com.project.echoproject.entity.UseAmount;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.UseAmountRepository;
import com.project.echoproject.repository.SiteUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
public class UseAmountServiceImplTest {

    @Autowired
    private UseAmountRepository useAmountRepository;

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Test
    @Commit
    public void testInsertUseAmountAndLogin() throws Exception {
        // 기존 사용자 조회
        SiteUser user = siteUserRepository.findByUserId("testUser")
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 이전 달 UseAmount 객체 생성 및 저장
        UseAmount previousMonthUseAmount = new UseAmount();
        previousMonthUseAmount.setSiteUser(user);
        previousMonthUseAmount.setUseDate(LocalDate.now().minusMonths(1));
        previousMonthUseAmount.setUseElectricity(100L);
        previousMonthUseAmount.setUseGas(50L);
        useAmountRepository.save(previousMonthUseAmount);

        // 현재 달 UseAmount 객체 생성 및 저장
        UseAmount currentMonthUseAmount = new UseAmount();
        currentMonthUseAmount.setSiteUser(user);
        currentMonthUseAmount.setUseDate(LocalDate.now());
        currentMonthUseAmount.setUseElectricity(120L);
        currentMonthUseAmount.setUseGas(60L);
        useAmountRepository.save(currentMonthUseAmount);
    }
}