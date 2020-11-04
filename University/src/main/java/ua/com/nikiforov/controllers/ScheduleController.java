package ua.com.nikiforov.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.nikiforov.controllers.model_atributes.ScheduleFindAttr;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.lesson.LessonInfo;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.subject.SubjectService;
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    private static final String TIMEZONE = "Europe/Simferopol";

    @ModelAttribute("scheduleFind")
    public ScheduleFindAttr getScheduleFindAttr() {
        return new ScheduleFindAttr();
    }

    private LessonService lessonService;

    private SubjectService subjectService;

    private TeacherService teacherService;

    private TeachersTimetableService teachersTimetableService;

    @Autowired
    public ScheduleController(LessonService lessonService, SubjectService subjectService, TeacherService teacherService,
            TeachersTimetableService teachersTimetableService) {
        this.lessonService = lessonService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
        this.teachersTimetableService = teachersTimetableService;
    }

    @GetMapping("/")
    public String show(Model model) {
        return "timetable/schedule";
    }

    @GetMapping("/teacher")
    public String teacherTimetable(Model model) {
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "timetable/teacher_timetable_form";
    }

    @GetMapping("/student")
    public String studentTimetable() {
        return "";
    }

    @PostMapping("/find")
    public String findSchedule(@ModelAttribute("scheduleFind") ScheduleFindAttr scheduleFindAttr, Model model) {
        Teacher teacher;
        try {
            teacher = teacherService.getTeacherByName(scheduleFindAttr.getFirstName(), scheduleFindAttr.getLastName());
            model.addAttribute("teacher", teacher);
        } catch (EntityNotFoundException e) {
            model.addAttribute("scheduleFindAttr", scheduleFindAttr);
            return "timetable/teacher_not_found";
        }
        List<Timetable> dayTimetable = teachersTimetableService.getDayTimetable(scheduleFindAttr.getTime(),
                teacher.getId());
        if (!dayTimetable.isEmpty()) {
            for (Timetable timetable : dayTimetable) {
                timetable.setLessonInfo(lessonService.getLessonInfoById(timetable.getLessonId()));
            }
            Timetable timetable = dayTimetable.get(0);
            Instant instant = timetable.getTime();
            ZonedDateTime zonedDateTime = getZonedDateTime(instant, TIMEZONE);
            int monthDay = zonedDateTime.getDayOfMonth();
            model.addAttribute("monthDay", monthDay);

            String month = zonedDateTime.getMonth().toString();
            model.addAttribute("month", month);

            int year = zonedDateTime.getYear();
            model.addAttribute("year", year);
            
            String weekDay = zonedDateTime.getDayOfWeek().name();
            model.addAttribute("weekDay", weekDay);
            model.addAttribute("dayTimetable", dayTimetable);
            return "timetable/teacher_schedule";
        } else {
            model.addAttribute("scheduleFindAttr", scheduleFindAttr);
            return "timetable/schedule_not_found";
        }
    }

    private ZonedDateTime getZonedDateTime(Instant instant, String zone) {
        ZoneId zoneId = ZoneId.of(zone);
        return ZonedDateTime.ofInstant(instant, zoneId);
    }
}
