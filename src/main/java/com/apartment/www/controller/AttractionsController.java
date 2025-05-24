package com.apartment.www.controller;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attractions")
public class AttractionsController {


    @GetMapping
    public String getHomepage() {
        return "attractions.html";
    }
}
