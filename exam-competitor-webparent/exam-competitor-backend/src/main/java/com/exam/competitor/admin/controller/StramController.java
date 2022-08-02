package com.exam.competitor.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class StramController {

	
	@GetMapping(value = "/videos/all")
    public String getAllVideo(Model model) {
        model.addAttribute("title", "tom-jerry");
        return "videos/all-videos";
    }
}
