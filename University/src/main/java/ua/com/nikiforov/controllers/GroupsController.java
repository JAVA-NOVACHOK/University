package ua.com.nikiforov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private StudentsService studentService;

    @GetMapping("/show")
    public String getGroups(Model model) {
//        groupService.addGroup("AA-16");
//        groupService.addGroup("AB-12");
//        groupService.addGroup("AC-10");
//        groupService.addGroup("AD-11");
//        groupService.addGroup("AF-42");
//        groupService.addGroup("AZ-62");
//        groupService.addGroup("AX-82");
//        groupService.addGroup("AW-12");
//        groupService.addGroup("AK-72");
//        studentService.deleteStudentById(3);
//        studentService.addStudent("Artem", "Nikiforov", 1);
//        studentService.addStudent("Bob", "Jackson", 1);
//        studentService.addStudent("Bill", "Clinton", 1);
//        studentService.addStudent("Jack", "Sparrow", 1);
//        studentService.addStudent("Will", "Smith", 1);

        List<Group> groups = groupService.getAllGroups();
        model.addAttribute("groups", groups);
        return "groups";
    }

}
