package com.project.echoproject.controller;

import com.project.echoproject.entity.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/eco_mall")
@Controller
@RequiredArgsConstructor
public class EcoMallController {

    @GetMapping("")
    public String ecoMallHome(Model model) {

        return "eco_mall";
    }


}
