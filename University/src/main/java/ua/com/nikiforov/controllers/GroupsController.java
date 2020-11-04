package ua.com.nikiforov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.services.group.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private GroupService groupService;

    @Autowired
    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/")
    public String getGroups(Model model) {

        List<Group> groups = groupService.getAllGroups();
        model.addAttribute("groups", groups);
        return "groups";
    }

}
