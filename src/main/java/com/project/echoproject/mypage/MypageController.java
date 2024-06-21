package com.project.echoproject.mypage;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final SiteUserRepository userRepository;

    @Autowired
    public MypageController(SiteUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/edit/{userId}")
    public String editPersonalInfo(@PathVariable String userId, Model model) {
        SiteUser user = new SiteUser();
        user.setUserId("user1");
        user.setUserName("user1");
        user.setPassword("test1234");
        user.setEmail("test@1234.com");
        user.setPhoneNum("010-1234-5678");
        user.setGender("여성");
        user.setImgUrl("https://example.com/profile.jpg");

        model.addAttribute("user", user);
        return "edit_form";
    }
}
