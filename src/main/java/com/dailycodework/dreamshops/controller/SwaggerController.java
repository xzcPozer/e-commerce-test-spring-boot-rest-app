package com.dailycodework.dreamshops.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {
    @GetMapping("/")
    public String redirectSwagger() {
        return "redirect:/swagger-ui.html";
    }
}
