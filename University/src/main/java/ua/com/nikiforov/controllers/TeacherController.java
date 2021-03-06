package ua.com.nikiforov.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherController.class);

    private static final String ROOMS_ATTR = "rooms";
    private static final String GROUPS_ATTR = "groups";
    private static final String TEACHERS_ATTR = "teachers";
    private static final String TEACHER_ATTR = "teacher";
    private static final String SUBJECTS_ATTR = "subjects";
    private static final String VIEW_TEACHERS = "teachers/teachers";
    private static final String VIEW_TEACHER_ONE = "teachers/one_teacher";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";

    private TeacherService teacherService;
    private SubjectService subjectService;
    private RoomService roomService;
    private GroupService groupService;

    @Autowired
    public TeacherController(TeacherService teacherService, SubjectService subjectService, RoomService roomService,
            GroupService groupService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.roomService = roomService;
        this.groupService = groupService;
    }

    @ModelAttribute("teacher")
    public TeacherDTO getTeacher() {
        return new TeacherDTO();
    }

    @GetMapping()
    public String show(Model model) {
        return VIEW_TEACHERS;
    }

    @GetMapping("/teacher")
    public String showTeacher(@RequestParam long id, Model model) {
        TeacherDTO teacher;
        try {
             teacher = teacherService.getTeacherById(id);
        }catch (EntityNotFoundException e){
            model.addAttribute(FAIL_MSG, String.format("Error! Failed to get teacher by id %d", id));
            return VIEW_TEACHERS;
        }
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        subjects.removeAll(teacher.getSubjects());
        model.addAttribute(TEACHER_ATTR, teacher);
        model.addAttribute(SUBJECTS_ATTR, subjects);
        model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        return VIEW_TEACHER_ONE;
    }

    @PostMapping("/add")
    public String processAdding(@Valid @ModelAttribute(TEACHER_ATTR) TeacherDTO teacher, Model model) {
        String firstName = teacher.getFirstName();
        String lastName = teacher.getLastName();
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        try {
            teacherService.addTeacher(teacher);
            model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
            model.addAttribute(SUCCESS_MSG, String.format("Teacher %s %s added successfully!", firstName, lastName));
        } catch (DuplicateKeyException e) {
            model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
            model.addAttribute(FAIL_MSG,
                    String.format("Warning! Teacher with %s %s already exists", firstName, lastName));
            return VIEW_TEACHERS;
        } catch (DataOperationException e) {
            model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
            model.addAttribute(FAIL_MSG, String.format("Warning! Failed to add Teacher %s %s ", firstName, lastName));
            return VIEW_TEACHERS;
        }
        return VIEW_TEACHERS;
    }

    @PostMapping("/edit")
    public String editTeacher(@Valid @ModelAttribute("teacher") TeacherDTO teacher, Model model) {
        String firstName = teacher.getFirstName();
        String lastName = teacher.getLastName();
        try {
            model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
            model.addAttribute(TEACHER_ATTR, teacher);
            try {
                teacherService.updateTeacher(teacher);
                model.addAttribute(SUCCESS_MSG,
                        String.format("Teacher %s %s changed successfully", firstName, lastName));
            } catch (DuplicateKeyException e) {
                model.addAttribute(FAIL_MSG,
                        String.format("Warning! Teacher %s %s already exists.", firstName, lastName));
                return VIEW_TEACHER_ONE;
            }
        } catch (DataAccessException e) {
            model.addAttribute(FAIL_MSG, String.format("Error! Failed to update teacher %s %s.", firstName, lastName));
            return VIEW_TEACHER_ONE;
        }
        model.addAttribute(TEACHER_ATTR, teacherService.getTeacherById(teacher.getId()));

        return VIEW_TEACHER_ONE;

    }

    @GetMapping("/delete")
    public String deleteTeacher(@RequestParam long id, Model model) {
        try {
            TeacherDTO teacher = teacherService.getTeacherById(id);
            String firstName = teacher.getFirstName();
            String lastName = teacher.getLastName();
            try {
                teacherService.deleteTeacherById(id);
                model.addAttribute(SUCCESS_MSG, String.format("Teacher %s %s deleted successfully!", firstName, lastName));
            } catch (DataOperationException e) {
                model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
                model.addAttribute(FAIL_MSG,
                        String.format("Warning! Failed to add Teacher %s %s ", firstName, lastName));
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Error! Failed to find Teacher with id %d", id));
        }
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        return VIEW_TEACHERS;
    }

    @PostMapping("/assign")
    public String assignSubjectToTeacher(@RequestParam int subjectId, @RequestParam long teacherId, Model model) {
        SubjectDTO subject = subjectService.getSubjectById(subjectId);
        TeacherDTO teacher = teacherService.getTeacherById(teacherId);
        String subjectName = subject.getName();
        String teachersName = String.format("%s %s", teacher.getFirstName(), teacher.getLastName());
        model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        model.addAttribute(TEACHER_ATTR, teacher);
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        if (subjectId == 0) {
            model.addAttribute(FAIL_MSG, "Warning! To assign teacher  to subject you must choose subject!");
            return VIEW_TEACHER_ONE;
        }
        try {
            teacher = teacherService.assignSubjectToTeacher(teacherId,subjectId);
            model.addAttribute(TEACHER_ATTR,teacher);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Subject %s successfully assigned to %s!", subjectName, teachersName));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Subject %s is already assigned to %s!", subjectName, teachersName));
            return VIEW_TEACHER_ONE;
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Couldn't assign subject %s to teacher %s!", subjectName, teachersName));
            return VIEW_TEACHER_ONE;
        }
        return VIEW_TEACHER_ONE;
    }

    @GetMapping("/unassign")
    public String unassignSubjectFromTeacher(@RequestParam int subjectId, @RequestParam long teacherId, Model model) {
        SubjectDTO subject = subjectService.getSubjectById(subjectId);
        TeacherDTO teacher = teacherService.getTeacherById(teacherId);
        String subjectName = subject.getName();
        String teachersName = String.format("%s %s", teacher.getFirstName(), teacher.getLastName());
        model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        model.addAttribute(TEACHER_ATTR, teacher);
        model.addAttribute(TEACHERS_ATTR, teacherService.getAllTeachers());
        model.addAttribute(SUBJECTS_ATTR, subjectService.getAllSubjects());
        try {
            teacher = teacherService.unassignSubjectFromTeacher(teacherId,subjectId);
            model.addAttribute(TEACHER_ATTR, teacher);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Subject %s successfully unassigned from %s!", subjectName, teachersName));
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Couldn't unassign subject %s from teacher %s!", subjectName, teachersName));
            return VIEW_TEACHER_ONE;
        }
        return VIEW_TEACHER_ONE;
    }

    @PostMapping("/find")
    public String findTeacher(@RequestParam String firstName, @RequestParam String lastName, Model model) {
        try {
            List<TeacherDTO> teachers = teacherService.getTeacherByLikeName(firstName, lastName);
            if (teachers.isEmpty()) {
                model.addAttribute(FAIL_MSG, "No teacher found!");
            }
            model.addAttribute(TEACHERS_ATTR, teachers);
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, String.format(
                    "ERROR! Couldn't fulfil search for teacher with searching parameters firstname = %s, lastname = %s!",
                    firstName, lastName));
            return VIEW_TEACHERS;
        }
        return VIEW_TEACHERS;
    }

}
