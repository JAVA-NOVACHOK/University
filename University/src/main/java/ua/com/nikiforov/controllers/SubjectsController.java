package ua.com.nikiforov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.services.subject.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectsController {

    private static final String MAPPING_SLASH = "/";
    private static final String SUBJECTS_ATTR = "subjects";
    private static final String VIEW_SUBJECTS = "subjects";

    private SubjectService subjectService;

    @Autowired
    public SubjectsController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping(MAPPING_SLASH)
    public String showSubjects(Model model) {

        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute(SUBJECTS_ATTR, subjects);
        return VIEW_SUBJECTS;

    }

}
