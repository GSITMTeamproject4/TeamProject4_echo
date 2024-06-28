package com.project.echoproject.controller;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UseAmount;
import com.project.echoproject.service.MypageService;
import com.project.echoproject.service.SiteUserServiceImpl;
import com.project.echoproject.service.UseAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping(value = {"/", "/index", "/main", "index"})
public class DashboardController {

    private final UseAmountService useAmountService;
    private final SiteUserServiceImpl siteUserServiceImpl;
    private final MypageService mypageService; // 추가

    @Autowired
    public DashboardController(UseAmountService useAmountService,
                               SiteUserServiceImpl siteUserServiceImpl,
                               MypageService mypageService) { // 생성자 매개변수 추가
        this.useAmountService = useAmountService;
        this.siteUserServiceImpl = siteUserServiceImpl;
        this.mypageService = mypageService; // 초기화
    }

    /**
     * 대시보드 페이지를 처리하는 메서드.
     * @param model 데이터를 뷰에 전달하기 위한 모델 객체
     * @param principal 현재 사용자 정보를 제공하는 Principal 객체
     * @return index.html 뷰 페이지 이름
     */



    @GetMapping(value = {"/", "/index", "/main", "index"})
    public String dashboard(Model model, Principal principal) {
        if (principal != null) {
            String userId = principal.getName();

            // 사용자 정보를 가져와 모델에 추가 (추가된 부분)
            SiteUser user = mypageService.getUserById(userId);
            model.addAttribute("user", user);

            LocalDate now = LocalDate.now();
            int currentMonth = now.getMonthValue();
            int currentYear = now.getYear();

            // 현재 연도 데이터 가져오기
            Map<Integer, UseAmount> useAmounts = useAmountService.getMonthlyUseAmounts(userId, currentYear);

            UseAmount currentUseAmount = useAmounts.get(currentMonth);
            UseAmount previousUseAmount = useAmounts.get(currentMonth - 1);

            if (currentUseAmount != null) {
                model.addAttribute("currentElectricity", currentUseAmount.getUseElectricity() + " (KWh)");
                model.addAttribute("currentGas", currentUseAmount.getUseGas() + " (M³)");
            }

            if (previousUseAmount != null) {
                double electricityDiff = calculateDifference(currentUseAmount.getUseElectricity(), previousUseAmount.getUseElectricity());
                double gasDiff = calculateDifference(currentUseAmount.getUseGas(), previousUseAmount.getUseGas());

                model.addAttribute("electricityDiff", String.format("%.1f%%", electricityDiff));
                model.addAttribute("gasDiff", String.format("%.1f%%", gasDiff));

                // Format the messages using formatDifferenceMessage method
                String electricityDiffMsg = formatDifferenceMessage(electricityDiff, "전기");
                String gasDiffMsg = formatDifferenceMessage(gasDiff, "가스");

                model.addAttribute("electricityDiffMsg", electricityDiffMsg);
                model.addAttribute("gasDiffMsg", gasDiffMsg);
            } else {
                model.addAttribute("electricityDiff", "N/A");
                model.addAttribute("gasDiff", "N/A");
                model.addAttribute("electricityDiffMsg", "");
                model.addAttribute("gasDiffMsg", "");
            }
        }

        return "index";
    }

    /**
     * 두 값을 비교하여 백분율 차이를 계산하는 메서드.
     * @param current 현재 값
     * @param previous 이전 값
     * @return 백분율 차이
     */
    private double calculateDifference(long current, long previous) {
        if (previous == 0) return 0;
        return ((double) (current - previous) / previous) * 100;
    }

    /**
     * 차이를 설명하는 메시지를 포맷하는 메서드.
     * @param diff 차이
     * @param type 데이터 유형 (전기 또는 가스)
     * @return 포맷된 메시지
     */
    private String formatDifferenceMessage(double diff, String type) {
        double absDiff = Math.abs(diff);
        if (diff > 0) {
            return type + " 사용량이 " + String.format("%.1f%%를 더 쓰셨네요!", absDiff);
        } else if (diff < 0) {
            return type + " 사용량이 " + String.format("%.1f%%를 줄이셨네요!", absDiff);
        } else {
            return type + " 사용량이 변화가 없습니다.";
        }
    }
}
