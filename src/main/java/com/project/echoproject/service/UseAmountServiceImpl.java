package com.project.echoproject.service;

import com.project.echoproject.dto.UseAmountForm;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UseAmount;
import com.project.echoproject.repository.SiteUserRepository;
import com.project.echoproject.repository.UseAmountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class UseAmountServiceImpl implements UseAmountService {

    private final UseAmountRepository useAmountRepository;
    private final SiteUserRepository siteUserRepository;

    @Autowired
    public UseAmountServiceImpl(UseAmountRepository useAmountRepository, SiteUserRepository siteUserRepository) {
        this.useAmountRepository = useAmountRepository;
        this.siteUserRepository = siteUserRepository;
    }

    @Override
    @Transactional
    //사용자의 전기/가스 사용량 데이터를 저장
    public void saveUseAmount(String userId, UseAmountForm useAmountForm) {
        SiteUser user = siteUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //useAmountForm에서 사용 날짜를 가져와 해당 월의 시작 날짜와 종료 날짜를 계산
        LocalDate useDate = useAmountForm.getUseDate();
        YearMonth yearMonth = YearMonth.from(useDate);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        //해당 월에 이미 사용량 데이터가 존재하는지 확인
        Optional<UseAmount> existingUseAmount = useAmountRepository.findFirstBySiteUserAndUseDateBetween(user, startDate, endDate);

        //기존 데이터가 있으면 업데이트하고, 없으면 새로운 UseAmount 객체를 생성
        UseAmount useAmount;
        if (existingUseAmount.isPresent()) {
            useAmount = existingUseAmount.get();
        } else {
            useAmount = new UseAmount();
            useAmount.setSiteUser(user);
        }

        useAmount.setUseElectricity(useAmountForm.getUseElectricity());
        useAmount.setUseGas(useAmountForm.getUseGas());
        useAmount.setUseDate(useAmountForm.getUseDate());
        useAmountRepository.save(useAmount);
    }


    @Override
    // 사용자의 특정 연도에 대한 월별 사용량 데이터를 조회
    public Map<Integer, UseAmount> getMonthlyUseAmounts(String userId, int year) {
        SiteUser user = siteUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //조회할 연도의 시작 날짜와 종료 날짜를 계산
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        //해당 연도의 모든 사용량 데이터를 조회
        List<UseAmount> useAmounts = useAmountRepository.findBySiteUserAndUseDateBetween(user, startOfYear, endOfYear);

        //조회된 데이터를 월별로 매핑하여 Map<Integer, UseAmount>에 저장
        Map<Integer, UseAmount> monthlyUseAmounts = new HashMap<>();
        for (UseAmount useAmount : useAmounts) {
            int month = useAmount.getUseDate().getMonthValue();
            monthlyUseAmounts.put(month, useAmount);
        }

        return monthlyUseAmounts;
    }
}

