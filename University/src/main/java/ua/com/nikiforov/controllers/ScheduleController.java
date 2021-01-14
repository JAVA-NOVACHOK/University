package ua.com.nikiforov.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;
import ua.com.nikiforov.services.timetables.DayTimetable;
import ua.com.nikiforov.services.timetables.StudentTimetableService;
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

    private static final String SCHEDULE_FIND_ATTR = "scheduleFindAttr";
    private static final String ROOMS_ATTR = "rooms";
    private static final String SUBJECTS_ATTR = "subjects";
    private static final String GROUPS_ATTR = "groups";
    private static final String TEACHERS_ATTR = "teachers";
    private static final String TEACHER_ATTR = "teacher";
    private static final String TIMETABLE_MODEL_ATTR = "timetable";
    private static final String LESSON_ATTR = "lesson";

    private static final String NOT_CLASSES_MSG = "There are no classes for %s %s on date %s.";
    private static final String TEACHER = "teacher";
    private static final String STUDENT = "student";
    private static final String DAY_TIMETABLE = "timetables";

    private static final String VIEW_SCHEDULE = "timetable/schedule";
    private static final String VIEW_TEACHER_TIMETABLE_FORM = "timetable/teacher_timetable_form";
    private static final String VIEW_STUDENT_TIMETABLE_FORM = "timetable/student_timetable_form";
    private static final String VIEW_TIMETABLE_EDIT_SCHEDULE = "timetable/edit_schedule";
    private static final String VIEW_STUDENT_SCHEDULE = "timetable/student_schedule";
    private static final String VIEW_TEACHER_SCHEDULE = "timetable/teacher_schedule";
    private static final String VIEW_TEACHER_ONE = "teachers/one_teacher";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private static final String ERROR_MSG_FIND_BY_ID = "ERROR! Couldn't find teacher by id %d.";

    private StudentsService studentsService;

    private TeacherService teacherService;

    private TeachersTimetableService teachersTimetableService;

    private StudentTimetableService studentTimetableService;

    private LessonService lessonService;

    private SubjectService subjectService;

    private RoomService roomService;

    private GroupService groupService;

    @Autowired
    public ScheduleController(StudentsService studentsService, TeacherService teacherService,
                              TeachersTimetableService teachersTimetableService, StudentTimetableService studentTimetableService,
                              LessonService lessonService, SubjectService subjectService, RoomService roomService,
                              GroupService groupService) {
        this.studentsService = studentsService;
        this.teacherService = teacherService;
        this.teachersTimetableService = teachersTimetableService;
        this.studentTimetableService = studentTimetableService;
        this.lessonService = lessonService;
        this.subjectService = subjectService;
        this.roomService = roomService;
        this.groupService = groupService;
    }

    @ModelAttribute(LESSON_ATTR)
    public LessonDTO getLesson() {
        return new LessonDTO();
    }

    @ModelAttribute(SCHEDULE_FIND_ATTR)
    public ScheduleFindAttr getScheduleFindAttr() {
        return new ScheduleFindAttr();
    }

    @ModelAttribute(TIMETABLE_MODEL_ATTR)
    public TimetableModelAttr getTimetableModelAttr() {
        return new TimetableModelAttr();
    }

    @GetMapping()
    public String show(Model model) {
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        return VIEW_SCHEDULE;
    }

    @GetMapping("/teacher")
    public String getTeacherTimetableForm() {
        return VIEW_TEACHER_TIMETABLE_FORM;
    }

    @PostMapping("/teachers_day")
    public String findTeacherDaySchedule(@ModelAttribute(SCHEDULE_FIND_ATTR) ScheduleFindAttr scheduleFindAttr,
                                         Model model) {
        String firstName = scheduleFindAttr.getFirstName();
        String lastName = scheduleFindAttr.getLastName();
        String date = scheduleFindAttr.getTime();
        TeacherDTO teacher;
        try {
            teacher = teacherService.getTeacherByName(firstName, lastName);
            model.addAttribute(TEACHER, teacher);
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot find teacher with name '%s %s! Check spelling.", firstName, lastName));
            return VIEW_TEACHER_TIMETABLE_FORM;
        }
        List<DayTimetable> timetables = teachersTimetableService.getDayTimetable(date, teacher.getId());
        if (timetables.isEmpty()) {
            model.addAttribute(FAIL_MSG,
                    String.format(NOT_CLASSES_MSG, firstName, lastName, date));
            return VIEW_TEACHER_TIMETABLE_FORM;
        }
        model.addAttribute(DAY_TIMETABLE, timetables);
        return VIEW_TEACHER_SCHEDULE;
    }

    @GetMapping("/student")
    public String getStudentTimetableForm() {
        return VIEW_STUDENT_TIMETABLE_FORM;
    }

    @PostMapping("/students_day")
    public String findStudentDaySchedule(@Valid @ModelAttribute(SCHEDULE_FIND_ATTR) ScheduleFindAttr scheduleFindAttr,
                                         Model model) {
        String firstName = scheduleFindAttr.getFirstName();
        String lastName = scheduleFindAttr.getLastName();
        String date = scheduleFindAttr.getTime();
        StudentDTO student;
        try {
            student = studentsService.getStudentByName(scheduleFindAttr.getFirstName(), scheduleFindAttr.getLastName());
            model.addAttribute(STUDENT, student);
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot find student with name '%s %s! Check spelling.", firstName, lastName));
            return VIEW_STUDENT_TIMETABLE_FORM;
        }
        List<DayTimetable> timetables = studentTimetableService.getDayTimetable(scheduleFindAttr.getTime(),
                student.getGroupId());
        if (timetables.isEmpty()) {
            model.addAttribute(FAIL_MSG,
                    String.format(NOT_CLASSES_MSG, firstName, lastName, date));
            return VIEW_STUDENT_TIMETABLE_FORM;
        }
        model.addAttribute(DAY_TIMETABLE, timetables);
        return VIEW_STUDENT_SCHEDULE;
    }

    @PostMapping("/students_month")
    public String findStudentsMonthSchedule(@Valid @ModelAttribute(SCHEDULE_FIND_ATTR) ScheduleFindAttr scheduleFindAttr,
                                            Model model) {
        String firstName = scheduleFindAttr.getFirstName();
        String lastName = scheduleFindAttr.getLastName();
        String date = scheduleFindAttr.getTime();
        StudentDTO student;
        try {
            student = studentsService.getStudentByName(scheduleFindAttr.getFirstName(), scheduleFindAttr.getLastName());
            model.addAttribute(STUDENT, student);
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot find student with name '%s %s! Check spelling.", firstName, lastName));
            return VIEW_STUDENT_TIMETABLE_FORM;
        }
        List<DayTimetable> timetables = studentTimetableService.getMonthTimetable(scheduleFindAttr.getTime(),
                student.getGroupId());
        if (timetables.isEmpty()) {
            model.addAttribute(FAIL_MSG,
                    String.format(NOT_CLASSES_MSG, firstName, lastName, date));
            return VIEW_STUDENT_TIMETABLE_FORM;
        }
        model.addAttribute(DAY_TIMETABLE, timetables);
        return VIEW_STUDENT_SCHEDULE;

    }

    @PostMapping("/teachers_month")
    public String findTeacherMonthSchedule(@Valid @ModelAttribute(SCHEDULE_FIND_ATTR) ScheduleFindAttr scheduleFindAttr,
                                           Model model) {
        String firstName = scheduleFindAttr.getFirstName();
        String lastName = scheduleFindAttr.getLastName();
        String date = scheduleFindAttr.getTime();
        TeacherDTO teacher;
        try {
            teacher = teacherService.getTeacherByName(firstName, lastName);
            model.addAttribute(TEACHER, teacher);
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot find teacher with name '%s %s! Check spelling.", firstName, lastName));
            return VIEW_TEACHER_SCHEDULE;
        }
        List<DayTimetable> timetables = teachersTimetableService.getMonthTimetable(date, teacher.getId());
        if (timetables.isEmpty()) {
            model.addAttribute(FAIL_MSG,
                    String.format(NOT_CLASSES_MSG, firstName, lastName, date));
            return VIEW_TEACHER_TIMETABLE_FORM;
        }
        model.addAttribute(DAY_TIMETABLE, timetables);
        return VIEW_TEACHER_SCHEDULE;
    }

    @PostMapping("/add")
    public String addSchedule(@Valid @ModelAttribute("lesson") LessonDTO lesson, Model model) {
        long teacherId = lesson.getTeacherId();
        try {
            model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
            model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
            model.addAttribute(TEACHER_ATTR, teacherService.getTeacherById(teacherId));
            lessonService.addLesson(lesson);
            model.addAttribute(SUCCESS_MSG, "Lesson successfully added to schedule!");
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, String.format(ERROR_MSG_FIND_BY_ID, teacherId));
            return VIEW_SCHEDULE;
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG, "Warning! Such lesson already exists in schedule.");
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, "ERROR! Couldn't add lesson to schedule!");
        }
        return VIEW_TEACHER_ONE;
    }

    @GetMapping("/edit")
    public String editSchedule(@Valid @ModelAttribute(TIMETABLE_MODEL_ATTR) TimetableModelAttr timetable, @RequestParam String dateString,
                               Model model) {
        try {
            model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
            model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
            model.addAttribute(TEACHER_ATTR, teacherService.getTeacherById(timetable.getTeacherId()));
            timetable.setDate(dateString);
            model.addAttribute(TIMETABLE_MODEL_ATTR, timetable);
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, String.format(ERROR_MSG_FIND_BY_ID, timetable.getTeacherId()));
            return VIEW_SCHEDULE;
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG, "Warning! Such lesson already exists in schedule.");
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, "ERROR! Couldn't add lesson to schedule!");
            return VIEW_SCHEDULE;
        }
        return VIEW_TIMETABLE_EDIT_SCHEDULE;
    }

    @PostMapping("/edit")
    public String processEdit(@ModelAttribute("lesson") LessonDTO lesson, Model model) {
        try {
            model.addAttribute(TEACHER_ATTR, teacherService.getTeacherById(lesson.getTeacherId()));
            lessonService.updateLesson(lesson);
            model.addAttribute(DAY_TIMETABLE, teachersTimetableService.getDayTimetable(lesson.getDate(), lesson.getTeacherId()));
            model.addAttribute(SUCCESS_MSG, "Seccess updating timetable!!!!");
        }catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, String.format("ERROR! Couldn't find teacher by id %d.", lesson.getTeacherId()));
            return VIEW_SCHEDULE;
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG, "Warning! Failed to update timetsble, already exists!!!!");
            return VIEW_TEACHER_SCHEDULE;
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, "Warning! Failed to update timetsble!!!!");
            return VIEW_TEACHER_SCHEDULE;
        }
        return VIEW_TEACHER_SCHEDULE;
    }

    @GetMapping("/delete")
    public String deleteLesson(@RequestParam long lessonId, Model model) {
        try {
            LessonDTO lesson = lessonService.getLessonById(lessonId);
            model.addAttribute(TEACHER_ATTR, teacherService.getTeacherById(lesson.getTeacherId()));
            lessonService.deleteLessonById(lessonId);
            model.addAttribute(DAY_TIMETABLE, teachersTimetableService.getDayTimetable(lesson.getDateFromLocalDate(), lesson.getTeacherId()));
            model.addAttribute(SUCCESS_MSG, "Successfully deleted timetable!");
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, String.format("ERROR! Couldn't find lesson by id %d.", lessonId));
            return VIEW_SCHEDULE;
        }catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, "Warning! Couldn't delete timetable!");
            return VIEW_TEACHER_SCHEDULE;
        }
        return VIEW_TEACHER_SCHEDULE;
    }

}
