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
    
    private static final String GROUP_ATTR = "group";
    private static final String STUDENTS_ATTR = "students";
    private static final String VIEW_STUDENTS = "students";

    private StudentsService studentService;

    private GroupService groupService;

    @Autowired
    public StudentsController(StudentsService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping()
    public String showStudents(@RequestParam long id, Model model) {

        Group group = groupService.getGroupById(id);
        List<Student> students = studentService.getStudentsByGroupId(id);
        Collections.sort(students);
        model.addAttribute(GROUP_ATTR, group);
        model.addAttribute(STUDENTS_ATTR, students);
        return VIEW_STUDENTS;
    }

}
