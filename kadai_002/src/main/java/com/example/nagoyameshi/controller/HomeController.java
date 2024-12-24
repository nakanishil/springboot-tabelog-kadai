package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }   
    
    @GetMapping("/company")
    public String showCompanyPage() {
        // templates/company.html を返却
        return "company";
    }
}