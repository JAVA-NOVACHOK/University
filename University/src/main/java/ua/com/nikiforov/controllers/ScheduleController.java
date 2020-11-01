package ua.com.nikiforov.controllers;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.nikiforov.controllers.model_atributes.ScheduleFindAttr;
import ua.com.nikiforov.dao.subject.SubjectDAO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.subject.SubjectService;
import ua.com.nikiforov.services.timetables.Period;
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private static final String TIMEZONE = "Europe/Simferopol";

    
    @ModelAttribute("scheduleFind")
    public ScheduleFindAttr getScheduleFindAttr() {
        return new ScheduleFindAttr();
    }

    private static final String DATE_1 = "2020-09-15";
    private static final String DATE_1_ADD_1_DAY = "2020-09-16";
    private static final String DATE_1_ADD_3_DAYS = "2020-09-18";
    private static final String DATE_1_ADD_13_DAYS = "2020-09-28";
    private static final String DATE_1_ADD_21_DAYS = "2020-10-06";
    private static final String DATE_1_ADD_33_DAYS = "2020-11-18";
    private static final String DATE = "2020-10-15";

//    @Autowired
    private LessonService lessonService;
//
//    @Autowired
    private SubjectService subjectService;
//
//    @Autowired
    private TeacherService teacherService;
//
//    @Autowired
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
//        lessonService.addLesson(1, 2, 3);
//        lessonService.addLesson(1, 3, 1);
//        lessonService.addLesson(1, 1, 2);
//        lessonService.addLesson(1, 4, 4);
//        lessonService.addLesson(2, 2, 2);
//        lessonService.addLesson(2, 1, 1);
//        lessonService.addLesson(2, 3, 3);
//        teachersTimetableService.addTimetable(1, 1, DATE, Period.FIRST);
//        teachersTimetableService.addTimetable(2, 1, DATE, Period.SECOND);
//        teachersTimetableService.addTimetable(3, 1, DATE, Period.THIRD);
//        teachersTimetableService.addTimetable(4, 1, DATE, Period.FOURTH);
//        teachersTimetableService.addTimetable(lessonId, teacherId, date, period)
//        teachersTimetableService.addTimetable(lessonId, teacherId, date, period)
//        teachersTimetableService.addTimetable(lessonId, teacherId, date, period)
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
        Teacher teacher = teacherService.getTeacherByName(scheduleFindAttr.getFirstName(),
                scheduleFindAttr.getLastName());
        List<Timetable> dayTimetable = teachersTimetableService.getDayTimetable(scheduleFindAttr.getTime(),
                teacher.getId());
        if (!dayTimetable.isEmpty()) {
            Instant instant = dayTimetable.get(0).getTime();
            ZonedDateTime zonedDateTime = getZonedDateTime(instant, TIMEZONE);
            String weekDay = zonedDateTime.getDayOfWeek().name();
            int monthDay = zonedDateTime.getDayOfMonth();
            model.addAttribute("weekDay", weekDay);
            model.addAttribute("monthDay", monthDay);
        }
        model.addAttribute("dayTimetable", dayTimetable);
        model.addAttribute("teacher", teacher);
        return "/";
    }

    private ZonedDateTime getZonedDateTime(Instant instant, String zone) {
        ZoneId zoneId = ZoneId.of(zone);
        return ZonedDateTime.ofInstant(instant, zoneId);
    }
}
