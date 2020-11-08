package ua.com.nikiforov.controllers;

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
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.timetables.DateInfo;
import ua.com.nikiforov.services.timetables.DayTimetable;
import ua.com.nikiforov.services.timetables.StudentTimetableService;
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

    private static final String SCHEDULE_FIND_ATTR = "scheduleFindAttr";
    private static final String TEACHER_NOT_FOUND_MSG = "Teacher not found from ScheduleConroller";
    private static final String STUDENT_NOT_FOUND_MSG = "Student not found from ScheduleConroller";
    private static final String TIMETABLE_TEACHER_NOT_FOUND = "timetable/teacher_not_found";
    private static final String TIMETABLE_SCHEDULE_NOT_FOUND = "timetable/schedule_not_found";
    private static final String TEACHER = "teacher";
    private static final String STUDENT = "student";
    private static final String DATE_INFO = "dateInfo";
    private static final String DAY_TIMETABLE = "dayTimetable";
    private static final String MONTH_TIMETABLE = "monthTimetable";
    
    private static final String MAPPING_SLASH = "/";
            private static final String MAPPING_TEACHER = "/teacher";
            private static final String MAPPING_SCHEDULE = "timetable/schedule";
            private static final String MAPPING_TEACHER_TIMETABLE_FORM = "timetable/teacher_timetable_form";

    @ModelAttribute(SCHEDULE_FIND_ATTR)
    public ScheduleFindAttr getScheduleFindAttr() {
        return new ScheduleFindAttr();
    }

    private StudentsService studentsService;

    private TeacherService teacherService;

    private TeachersTimetableService teachersTimetableService;

    private StudentTimetableService studentTimetableService;

    @Autowired
    public ScheduleController(StudentsService studentsService, TeacherService teacherService,
            TeachersTimetableService teachersTimetableService, StudentTimetableService studentTimetableService) {
        this.studentsService = studentsService;
        this.teacherService = teacherService;
        this.teachersTimetableService = teachersTimetableService;
        this.studentTimetableService = studentTimetableService;
    }

    @GetMapping(MAPPING_SLASH)
    public String show() {
        return MAPPING_SCHEDULE;
    }

    @GetMapping(MAPPING_TEACHER)
    public String teacherTimetable() {
        return MAPPING_TEACHER_TIMETABLE_FORM;
    }

    @PostMapping("/teachers_day")
    public String findTeacherDaySchedule(@ModelAttribute(SCHEDULE_FIND_ATTR) ScheduleFindAttr scheduleFindAttr,
            Model model) {
        Teacher teacher;
        try {
            teacher = teacherService.getTeacherByName(scheduleFindAttr.getFirstName(), scheduleFindAttr.getLastName());
            model.addAttribute(TEACHER, teacher);
        } catch (EntityNotFoundException e) {
            LOGGER.error(TEACHER_NOT_FOUND_MSG);
            model.addAttribute(SCHEDULE_FIND_ATTR, scheduleFindAttr);
            return TIMETABLE_TEACHER_NOT_FOUND;
        }
        List<Timetable> dayTimetable = teachersTimetableService.getDayTimetable(scheduleFindAttr.getTime(),
                teacher.getId());
        if (dayTimetable.isEmpty()) {
            model.addAttribute(SCHEDULE_FIND_ATTR, scheduleFindAttr);
            return TIMETABLE_SCHEDULE_NOT_FOUND;
        }
        Timetable timetable = dayTimetable.get(0);
        DateInfo dateInfo = teachersTimetableService.parseInstantToDateInfo(timetable);
        model.addAttribute(DATE_INFO, dateInfo);
        model.addAttribute(DAY_TIMETABLE, dayTimetable);
        return "timetable/teacher_schedule";
    }

    @GetMapping("/student")
    public String studentTimetable() {
        return "timetable/student_timetable_form";
    }

    @PostMapping("/students_day")
    public String findStudentDaySchedule(@ModelAttribute("scheduleFindAttr") ScheduleFindAttr scheduleFindAttr,
            Model model) {
        Student student;
        try {
            student = studentsService.getStudentByName(scheduleFindAttr.getFirstName(), scheduleFindAttr.getLastName());
            model.addAttribute(STUDENT, student);
        } catch (EntityNotFoundException e) {
            LOGGER.error(STUDENT_NOT_FOUND_MSG);
            model.addAttribute(SCHEDULE_FIND_ATTR, scheduleFindAttr);
            return TIMETABLE_TEACHER_NOT_FOUND;
        }
        List<Timetable> dayTimetable = studentTimetableService.getDayTimetable(scheduleFindAttr.getTime(),
                student.getGroupId());
        if (dayTimetable.isEmpty()) {
            model.addAttribute(SCHEDULE_FIND_ATTR, scheduleFindAttr);
            return TIMETABLE_SCHEDULE_NOT_FOUND;
        }
        Timetable timetable = dayTimetable.get(0);
        DateInfo dateInfo = studentTimetableService.parseInstantToDateInfo(timetable);
        model.addAttribute(DATE_INFO, dateInfo);
        model.addAttribute(DAY_TIMETABLE, dayTimetable);
        return "timetable/student_schedule";
    }

    @PostMapping("/students_month")
    public String findStudentsMonthSchedule(@ModelAttribute(SCHEDULE_FIND_ATTR) ScheduleFindAttr scheduleFindAttr,
            Model model) {
        Student student;
        try {
            student = studentsService.getStudentByName(scheduleFindAttr.getFirstName(), scheduleFindAttr.getLastName());
            model.addAttribute(STUDENT, student);
        } catch (EntityNotFoundException e) {
            LOGGER.error(TEACHER_NOT_FOUND_MSG);
            model.addAttribute(SCHEDULE_FIND_ATTR, scheduleFindAttr);
            return TIMETABLE_TEACHER_NOT_FOUND;
        }
        List<DayTimetable> monthTimetable = studentTimetableService.getMonthTimetable(scheduleFindAttr.getTime(),
                student.getGroupId());
        if (monthTimetable.isEmpty()) {
            model.addAttribute(SCHEDULE_FIND_ATTR, scheduleFindAttr);
            return TIMETABLE_SCHEDULE_NOT_FOUND;
        }
        model.addAttribute(MONTH_TIMETABLE, monthTimetable);
        return "timetable/student_schedule_month";

    }
    
    @PostMapping("/teachers_month")
    public String findMonthSchedule(@ModelAttribute(SCHEDULE_FIND_ATTR) ScheduleFindAttr scheduleFindAttr,
            Model model) {
        Teacher teacher;
        try {
            teacher = teacherService.getTeacherByName(scheduleFindAttr.getFirstName(), scheduleFindAttr.getLastName());
            model.addAttribute(TEACHER, teacher);
        } catch (EntityNotFoundException e) {
            LOGGER.error(TEACHER_NOT_FOUND_MSG);
            model.addAttribute(SCHEDULE_FIND_ATTR, scheduleFindAttr);
            return TIMETABLE_TEACHER_NOT_FOUND;
        }
        List<DayTimetable> monthTimetable = teachersTimetableService.getMonthTimetable(scheduleFindAttr.getTime(),
                teacher.getId());
        if (monthTimetable.isEmpty()) {
            model.addAttribute(SCHEDULE_FIND_ATTR, scheduleFindAttr);
            return TIMETABLE_SCHEDULE_NOT_FOUND;
        }
        model.addAttribute(MONTH_TIMETABLE, monthTimetable);
        return "timetable/teacher_schedule_month";
        
    }

}
