package ua.com.nikiforov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.subject.SubjectService;

import javax.validation.Valid;

@Controller
@RequestMapping("/subjects")
public class SubjectsController {

    private static final String SUBJECTS_ATTR = "subjects";
    private static final String SUBJECT_ATTR = "subject";
    private static final String TEACHERS_ATTR = "teachers";
    private static final String VIEW_SUBJECTS = "subjects/subjects";
    private static final String VIEW_SUBJECTS_EDIT_FORM = "subjects/edit_subject_form";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";

    private SubjectService subjectService;
    private TeacherService teacherService;

    @Autowired
    public SubjectsController(SubjectService subjectService, TeacherService teacherService) {
        this.subjectService = subjectService;
        this.teacherService = teacherService;
    }

    @ModelAttribute("subject")
    public SubjectDTO getSubject() {
        return new SubjectDTO();
    }

    @GetMapping()
    public String showSubjects(Model model) {
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        return VIEW_SUBJECTS;
    }

    @PostMapping("/add")
    public String processSubject(@Valid @ModelAttribute(SUBJECT_ATTR) SubjectDTO subject,Model model) {
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        try {
            subjectService.addSubject(subject);
        } catch (DuplicateKeyException e) {
            model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
            model.addAttribute(FAIL_MSG, String.format("Warning! Subject with name'%s' already exists", subject.getName()));
            return VIEW_SUBJECTS;
        } catch (DataOperationException e) {
            model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
            model.addAttribute(FAIL_MSG, String.format("Warning! Failed to add subject with name '%s' ", subject.getName()));
            return VIEW_SUBJECTS;
        }
        model.addAttribute(SUCCESS_MSG, String.format("Subject with name '%s' successfully added!", subject.getName()));
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        return VIEW_SUBJECTS;
    }

    @GetMapping("/edit")
    public String editSubject(@RequestParam int id, Model model) {
        model.addAttribute(SUBJECT_ATTR, subjectService.getSubjectById(id));
        return VIEW_SUBJECTS_EDIT_FORM;
    }

    @PostMapping("/edit")
    public String processEditing(@Valid @ModelAttribute(SUBJECT_ATTR) SubjectDTO subject, Model model) {
        SubjectDTO oldSubject;
        String subjectName = subject.getName();
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        try {
            subjectService.updateSubject(subject);
        } catch (DuplicateKeyException e) {
            oldSubject = subjectService.getSubjectById(subject.getId());
            model.addAttribute(SUBJECT_ATTR, oldSubject);
            model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
            model.addAttribute(FAIL_MSG, String.format("Warning! Subject with name'%s' already exists", subjectName));
            return VIEW_SUBJECTS;
        } catch (DataOperationException e) {
            oldSubject = subjectService.getSubjectById(subject.getId());
            model.addAttribute(SUBJECT_ATTR, oldSubject);
            model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
            model.addAttribute(FAIL_MSG,
                    String.format("Warning! Failed to change subject with name '%s' ", subjectName));
            return VIEW_SUBJECTS;
        }
        model.addAttribute(SUCCESS_MSG, String.format("Subject with name '%s' successfully updated!", subjectName));
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        return VIEW_SUBJECTS;
    }

    @GetMapping("/delete")
    public String processDelete(@RequestParam int id, Model model) {
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        SubjectDTO subject;
        try {
            subject = subjectService.getSubjectById(id);
            subjectService.deleteSubjectById(id);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Subject with name '%s' successfully deleted!", subject.getName()));
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, "Warning! Failed to delete subject!");
            model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
            return VIEW_SUBJECTS;
        }
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        return VIEW_SUBJECTS;
    }

    @PostMapping("/assign")
    public String assignSubjectToTeacher(@RequestParam int subjectId, @RequestParam long teacherId, Model model) {
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        if (teacherId == 0) {
            model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
            model.addAttribute(FAIL_MSG, "Warning! To assign subject to teacher you must choose teacher!");
            return VIEW_SUBJECTS;
        }
        SubjectDTO subject = subjectService.getSubjectById(subjectId);
        TeacherDTO teacher = teacherService.getTeacherById(teacherId);
        String subjectName = subject.getName();
        String teachersName = String.format("%s %s", teacher.getFirstName(), teacher.getLastName());
        try {
            teacherService.assignSubjectToTeacher(teacherId,subjectId);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Subject %s successfully assigned to %s!", subjectName, teachersName));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Subject %s is already assigned to %s!", subjectName, teachersName));
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Couldn't assign subject %s to teacher %s!", subjectName, teachersName));
        }
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        return VIEW_SUBJECTS;
    }

    @GetMapping("/unassign")
    public String unassignSubjectFromTeacher(@RequestParam int subjectId, @RequestParam long teacherId, Model model) {
        SubjectDTO subject = subjectService.getSubjectById(subjectId);
        TeacherDTO teacher = teacherService.getTeacherById(teacherId);
        String subjectName = subject.getName();
        String teachersName = String.format("%s %s", teacher.getFirstName(), teacher.getLastName());
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        try {
            teacherService.unassignSubjectFromTeacher(teacherId,subjectId);
            model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
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
