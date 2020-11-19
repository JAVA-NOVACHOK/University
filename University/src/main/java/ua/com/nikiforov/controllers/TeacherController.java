package ua.com.nikiforov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.subject.SubjectService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private static final String TEACHERS_ATTR = "teachers";
    private static final String SUBJECTS_ATTR = "subjects";
    private static final String ACTION_ATTR = "action";
    private static final String VIEW_TEACHERS = "teachers/teachers";
    private static final String VIEW_TEACHER_FORM = "teachers/teacher_form";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";

    private TeacherService teacherService;
    private SubjectService subjectService;

    @Autowired
    public TeacherController(TeacherService teacherServise, SubjectService subjectService) {
        this.teacherService = teacherServise;
        this.subjectService = subjectService;
    }

    @ModelAttribute("teacher")
    public Teacher getTeacher() {
        return new Teacher();
    }

    @GetMapping()
    public String show(Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute(TEACHERS_ATTR, teachers);
        return VIEW_TEACHERS;
    }

    @GetMapping("/add")
    public String addTeacher(Model model) {
        model.addAttribute(ACTION_ATTR, "adding");
        return VIEW_TEACHER_FORM;
    }

    @PostMapping("/add")
    public String processAdding(@RequestParam String firstName, @RequestParam String lastName, Model model) {
        try {
            teacherService.addTeacher(firstName, lastName);
            model.addAttribute(SUCCESS_MSG, String.format("Teacher %s %s added successfully!", firstName, lastName));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Warning! Teacher with %s %s already exists", firstName, lastName));
            return VIEW_TEACHERS;
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, String.format("Warning! Failed to add Teacher %s %s ", firstName, lastName));
            return VIEW_TEACHERS;
        }
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        return VIEW_TEACHERS;
    }

    @PostMapping("/edit")
    public String editTeacher(@ModelAttribute("teacher") Teacher teacher, Model model) {
        String firstName = teacher.getFirstName();
        String lastName = teacher.getLastName();
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        try {
            teacherService.updateTeacher(teacher);
            model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
            model.addAttribute(SUCCESS_MSG, String.format("Teacher %s %s changed successfully", firstName, lastName));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG, String.format("Warning! Teacher %s %s already exists.", firstName, lastName));
            return VIEW_TEACHERS;
        } catch (DataAccessException e) {
            model.addAttribute(FAIL_MSG, String.format("Failed to update teacher %s %s.", firstName, lastName));
            return VIEW_TEACHERS;
        }
        return VIEW_TEACHERS;

    }

    @GetMapping("/delete")
    public String deleteTeacher(@RequestParam long id, Model model) {
        Teacher teacher = teacherService.getTeacherById(id);
        String firstName = teacher.getFirstName();
        String lastName = teacher.getLastName();
        try {
            teacherService.deleteTeacherById(id);
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, String.format("Warning! Failed to add Teacher %s %s ", firstName, lastName));
            return VIEW_TEACHERS;
        }
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        model.addAttribute(SUCCESS_MSG, String.format("Teacher %s %s deleted successfully!", firstName, lastName));
        return VIEW_TEACHERS;
    }

    @PostMapping("/assign")
    public String assignSubjectToTeacher(@RequestParam int subjectId, @RequestParam long teacherId, Model model) {
        Subject subject = subjectService.getSubjectById(subjectId);
        Teacher teacher = teacherService.getTeacherById(teacherId);
        String subjectName = subject.getName();
        String teachersName = String.format("%s %s", teacher.getFirstName(), teacher.getLastName());
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        if (teacherId == 0) {
            model.addAttribute(FAIL_MSG, "Warning! To assign teacher  to subject you must choose subject!");
            return VIEW_TEACHERS;
        }
        try {
            teacherService.assignSubjectToTeacher(subjectId, teacherId);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Subject %s successfully assigned to %s!", subjectName, teachersName));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Subject %s is already assigned to %s!", subjectName, teachersName));
            return VIEW_TEACHERS;
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Couldn't assign subject %s to teacher %s!", subjectName, teachersName));
            return VIEW_TEACHERS;
        }
        return VIEW_TEACHERS;
    }

    @GetMapping("/unassign")
    public String unassignSubjectFromTeacher(@RequestParam int subjectId, @RequestParam long teacherId, Model model) {
        Subject subject = subjectService.getSubjectById(subjectId);
        Teacher teacher = teacherService.getTeacherById(teacherId);
        String subjectName = subject.getName();
        String teachersName = String.format("%s %s", teacher.getFirstName(), teacher.getLastName());
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        try {
            teacherService.unassignSubjectFromTeacher(subjectId, teacherId);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Subject %s successfully unassigned from %s!", subjectName, teachersName));
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Couldn't unassign subject %s from teacher %s!", subjectName, teachersName));
            return VIEW_TEACHERS;
        }
        return VIEW_TEACHERS;
    }

}
