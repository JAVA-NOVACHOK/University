package ua.com.nikiforov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.services.group.GroupService;


@Controller
public class UniversityController {
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private TableCreator tableCreator;

    @GetMapping("/start")
    public String start() {
        return "start";
    }
    
    @GetMapping("/start2")
    public String startPage() {
        return "start2";
    }
    @GetMapping("/start3")
    public String startPage3() {
        return "start3";
    }
    
    @GetMapping("/create")
    public String create() {
        tableCreator.createTables();
        return "created";
    }
}
