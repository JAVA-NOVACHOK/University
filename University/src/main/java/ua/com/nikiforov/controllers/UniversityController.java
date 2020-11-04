package ua.com.nikiforov.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UniversityController {

   

    @GetMapping("/")
    public String start() {
        return "start";
    }

}
