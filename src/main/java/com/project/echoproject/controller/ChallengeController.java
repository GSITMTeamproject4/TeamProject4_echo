package com.project.echoproject.controller;

import com.project.echoproject.dto.ChallengeDTO;
import com.project.echoproject.entity.Challenge;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.ChallengeService;
import com.project.echoproject.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;
    private final SiteUserService siteUserService;

    private static final Logger log = LoggerFactory.getLogger(ChallengeController.class);

    @GetMapping("")
    public String start(Model model,Principal principal) {
        SiteUser siteUser = siteUserService.findByUserId(principal.getName());
        model.addAttribute("challenge", new Challenge());
        return "challenge";
    }
    @GetMapping("/state")
    public @ResponseBody List<Map<String, Object>> monthPlan(Principal principal) {
        SiteUser siteUser = siteUserService.findByUserId(principal.getName());
            return challengeService.getChallengeList(siteUser);
    }
    @GetMapping("/add")
    public String add() {
        return "challengeAdd";
    }
}
