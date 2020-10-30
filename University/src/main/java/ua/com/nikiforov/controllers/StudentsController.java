package ua.com.nikiforov.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;



@Controller
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    private StudentsService studentService;

    @Autowired
    private GroupService groupService;

    @GetMapping("/")
    public String showStudents(@RequestParam long id, Model model) {

        Group group = groupService.getGroupById(id);
        List<Student> students = studentService.getStudentsByGroupId(id);
        Collections.sort(students);
        model.addAttribute("group", group);
        model.addAttribute("students", students);
        return "students";
    }

}
