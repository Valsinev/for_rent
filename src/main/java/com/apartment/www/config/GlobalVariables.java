package com.apartment.www.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalVariables {

    @Value("${OWNER_PHONE_NUMBER}")
    private String phoneNumber;

    @Value("${OWNER_EMAIL_ADDRESS}")
    private String emailAddress;

    @Value("${GOOGLE_MAPS_LINK}")
    private String googleMapsLink;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("ownerPhone", phoneNumber);
        model.addAttribute("ownerEmail", emailAddress);
        model.addAttribute("googleMapsLink", googleMapsLink);
    }
}

