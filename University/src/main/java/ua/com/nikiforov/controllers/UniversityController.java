package ua.com.nikiforov.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UniversityController {

    private static final String VIEW_WELCOME = "welcome_page";

    @GetMapping()
    public String showWelcomePage() {

        return VIEW_WELCOME;
    }

}
