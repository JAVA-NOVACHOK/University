package ua.com.nikiforov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.nikiforov.controllers.dto.SubjectDTO;
import ua.com.nikiforov.controllers.dto.TeacherDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.subject.SubjectService;

@Controller
@RequestMapping("/teachers_subjects")
public class TeachersSubjectsController {

    private static final String TEACHERS_ATTR = "teachers";
    private static final String SUBJECTS_ATTR = "subjects";
    private static final String VIEW_SUBJECTS = "subjects/subjects";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";

    private TeacherService teacherService;
    private SubjectService subjectService;

    @Autowired
    public TeachersSubjectsController(TeacherService teacherService, SubjectService subjectService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    @PostMapping("/assign")
    public String assignSubjectToTeacher(@RequestParam int subjectId, @RequestParam long teacherId, Model model) {
        SubjectDTO subject = subjectService.getSubjectById(subjectId);
        TeacherDTO teacher = teacherService.getTeacherById(teacherId);
        String subjectName = subject.getName();
        String teachersName = String.format("%s %s", teacher.getFirstName(), teacher.getLastName());
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        if (teacherId == 0) {
            model.addAttribute(FAIL_MSG, "Warning! To assign subject to teacher you must choose teacher!");
            return VIEW_SUBJECTS;
        }
        try {
            teacherService.assignSubjectToTeacher(subjectId, teacherId);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Subject %s successfully assigned to %s!", subjectName, teachersName));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Subject %s is already assigned to %s!", subjectName, teachersName));
            return VIEW_SUBJECTS;
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Couldn't assign subject %s to teacher %s!", subjectName, teachersName));
            return VIEW_SUBJECTS;
        }
        return VIEW_SUBJECTS;
    }

    @GetMapping("/unassign")
    public String unassignSubjectFromTeacher(@RequestParam int subjectId, @RequestParam long teacherId, Model model) {
        SubjectDTO subject = subjectService.getSubjectById(subjectId);
        TeacherDTO teacher = teacherService.getTeacherById(teacherId);
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
            return VIEW_SUBJECTS;
        }
        return VIEW_SUBJECTS;
    }

}
