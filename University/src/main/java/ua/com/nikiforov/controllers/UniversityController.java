package ua.com.nikiforov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ua.com.nikiforov.services.group.GroupService;

@Controller
public class UniversityController {

    @GetMapping("/start")
    public String start() {
        return "start";
    }

}
