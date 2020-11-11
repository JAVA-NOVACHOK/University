package ua.com.nikiforov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.services.persons.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private static final String TEACHERS_ATTR = "teachers";
    private static final String VIEW_TEACHERS = "teachers";

    private TeacherService teacherServise;

    @Autowired
    public TeacherController(TeacherService teacherServise) {
        this.teacherServise = teacherServise;
    }

    @GetMapping()
    public String show(Model model) {
        List<Teacher> teachers = teacherServise.getAllTeachers();
        model.addAttribute(TEACHERS_ATTR, teachers);
        return VIEW_TEACHERS;
    }

}
