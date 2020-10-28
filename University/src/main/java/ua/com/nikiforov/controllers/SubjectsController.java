package ua.com.nikiforov.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.subject.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectsController {
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/show")
    public String showSubjects(Model model) {
//        subjectService.addSubject("Math");
//        subjectService.addSubject("Programming Java");
//        subjectService.addSubject("Programming C#");
//        subjectService.addSubject("Programming JavaScript");
//        subjectService.addSubject("UI/UX");
//        subjectService.addSubject("Programming Pithon");
//        teacherService.addTeacher("Bob", "Nikiforov");
//        teacherService.addTeacher("Artem", "Jackson");
//        teacherService.addTeacher("Jack", "Clinton");
//        teacherService.addTeacher("Bill", "Sparrow");
//        teacherService.addTeacher("Jack", "Smith");
//        teacherService.assignSubjectToTeacher(1, 1);
//        teacherService.assignSubjectToTeacher(2, 2);
//        teacherService.assignSubjectToTeacher(3, 2);
//        teacherService.assignSubjectToTeacher(4, 3);
//        teacherService.assignSubjectToTeacher(5, 4);
        
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "subjects";
        
    }

}
