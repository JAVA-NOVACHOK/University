package ua.com.nikiforov.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

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
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.subject.SubjectService;
import ua.com.nikiforov.services.timetables.DateInfo;
import ua.com.nikiforov.services.timetables.DayTimetable;
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

    private static final String TIMEZONE = "Europe/Simferopol";

    @ModelAttribute("scheduleFindAttr")
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
    public String show() {
        return "timetable/schedule";
    }

    @GetMapping("/teacher")
    public String teacherTimetable() {
        return "timetable/teacher_timetable_form";
    }

    @GetMapping("/student")
    public String studentTimetable() {
        return "";
    }

    @PostMapping("/day")
    public String findDaySchedule(@ModelAttribute("scheduleFindAttr") ScheduleFindAttr scheduleFindAttr, Model model) {
        Teacher teacher;
        try {
            teacher = teacherService.getTeacherByName(scheduleFindAttr.getFirstName(), scheduleFindAttr.getLastName());
            model.addAttribute("teacher", teacher);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Teacher not found in ScheduleConroller");
            model.addAttribute("scheduleFindAttr", scheduleFindAttr);
            return "timetable/teacher_not_found";
        }
        List<Timetable> dayTimetable = teachersTimetableService.getDayTimetable(scheduleFindAttr.getTime(),
                teacher.getId());
        if (dayTimetable.isEmpty()) {
            model.addAttribute("scheduleFindAttr", scheduleFindAttr);
            return "timetable/schedule_not_found";
        }
        Timetable timetable = dayTimetable.get(0);
        DateInfo dateInfo = teachersTimetableService.parseInstantToDateInfo(timetable);
        model.addAttribute("dateInfo", dateInfo);
        model.addAttribute("dayTimetable", dayTimetable);
        return "timetable/teacher_schedule";

    }
    
    @PostMapping("/month")
    public String findMonthSchedule(@ModelAttribute("scheduleFindAttr") ScheduleFindAttr scheduleFindAttr, Model model) {
        Teacher teacher;
        try {
            teacher = teacherService.getTeacherByName(scheduleFindAttr.getFirstName(), scheduleFindAttr.getLastName());
            model.addAttribute("teacher", teacher);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Teacher not found in ScheduleConroller");
            model.addAttribute("scheduleFindAttr", scheduleFindAttr);
            return "timetable/teacher_not_found";
        }
        List<DayTimetable> monthTimetable = teachersTimetableService.getMonthTimetable(scheduleFindAttr.getTime(),
                teacher.getId());
        if (monthTimetable.isEmpty()) {
            model.addAttribute("scheduleFindAttr", scheduleFindAttr);
            return "timetable/schedule_not_found";
        }
        model.addAttribute("monthTimetable", monthTimetable);
        return "timetable/teacher_schedule_month";

    }

    private ZonedDateTime getZonedDateTime(Instant instant, String zone) {
        ZoneId zoneId = ZoneId.of(zone);
        return ZonedDateTime.ofInstant(instant, zoneId);
    }
}
