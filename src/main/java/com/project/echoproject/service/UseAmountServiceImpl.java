package com.project.echoproject.service;

import com.project.echoproject.dto.UseAmountForm;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UseAmount;
import com.project.echoproject.repository.UseAmountRepository;
import com.project.echoproject.repository.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void saveUseAmount(String userId, UseAmountForm useAmountForm) {
        SiteUser user = siteUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UseAmount useAmount = useAmountRepository.findBySiteUser(user);
        if (useAmount == null) {
            useAmount = new UseAmount();
            useAmount.setSiteUser(user);
        }

        useAmount.setUseElectricity(useAmountForm.getUseElectricity());
        useAmount.setUseGas(useAmountForm.getUseGas());

        useAmountRepository.save(useAmount);
    }
}