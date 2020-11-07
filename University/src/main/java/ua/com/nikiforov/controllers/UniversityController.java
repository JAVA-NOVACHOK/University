package ua.com.nikiforov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.timetables.Period;

@Controller
public class UniversityController {
    
    @Autowired
    private LessonService lessonService;

    @GetMapping("/")
    public String showWelcomePage() {
        
        
//        lessonService.addLesson(Period.FIRST, 1, 1, 1, "2020-11-15", 1);
//        lessonService.addLesson(Period.SECOND, 2, 2, 2, "2020-11-15", 1);
//        lessonService.addLesson(Period.THIRD, 1, 3, 3, "2020-11-15", 1);
//        lessonService.addLesson(Period.FOURTH, 2, 1, 4, "2020-11-15", 1);
//        lessonService.addLesson(Period.FIRST, 1, 1, 5, "2020-11-16", 1);
//        lessonService.addLesson(Period.SECOND, 2, 2, 6, "2020-11-16", 1);
//        lessonService.addLesson(Period.THIRD, 1, 3, 7, "2020-11-16", 1);
//        lessonService.addLesson(Period.FIRST, 2, 2, 8, "2020-11-17", 1);
//        lessonService.addLesson(Period.FIRST, 1, 1, 1, "2020-11-11", 1);
//        lessonService.addLesson(Period.SECOND, 2, 2, 2, "2020-11-11", 1);
//        lessonService.addLesson(Period.THIRD, 1, 3, 3, "2020-11-11", 1);
//        lessonService.addLesson(Period.FOURTH, 2, 1, 4, "2020-11-11", 1);
//        lessonService.addLesson(Period.FIRST, 1, 1, 5, "2020-11-12", 1);
//        lessonService.addLesson(Period.SECOND, 2, 2, 6, "2020-11-12", 1);
//        lessonService.addLesson(Period.THIRD, 1, 3, 7, "2020-11-12", 1);
//        lessonService.addLesson(Period.FIRST, 2, 2, 8, "2020-11-13", 1);
        return "welcome_page";
    }

}
