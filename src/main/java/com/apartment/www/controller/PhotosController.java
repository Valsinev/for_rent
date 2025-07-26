package com.apartment.www.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/photos")
public class PhotosController {

    @Value("${photos.folder}")
    private String photosFolder;

    @GetMapping
    public String showPhotos(Model model) {

        File folder = new File(photosFolder.replace("file:", ""));
        List<String> imageNames = new ArrayList<>();

        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile() && isImage(file.getName())) {
                    imageNames.add(file.getName());
                }
            }
        }
        model.addAttribute("imageNames", imageNames);

        return "photos";
    }

    private boolean isImage(String name) {
        return name.matches(".*\\.(jpg|jpeg|png|gif|webp)$");
    }


}
