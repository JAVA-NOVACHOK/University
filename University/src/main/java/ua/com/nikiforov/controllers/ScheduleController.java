package ua.com.nikiforov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.nikiforov.dao.subject.SubjectDAO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.services.subject.SubjectService;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private SubjectDAO subjectDao;
    

    @GetMapping("/")
    public String show(Model model) {
        return "schedule";
    }
    
    @GetMapping("/teacher")
    public String teacherTimetable(Model model) {
        List<Subject> subjects = subjectDao.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "teacher_timetable_form";
    }
    
    @GetMapping("/student")
    public String studentTimetable() {
        return "";
    }
    

}
