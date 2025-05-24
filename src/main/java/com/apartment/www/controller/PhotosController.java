package com.apartment.www.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/photos")
public class PhotosController {

    @GetMapping
    public String getHomepage() {
        return "photos.html";
    }
}
