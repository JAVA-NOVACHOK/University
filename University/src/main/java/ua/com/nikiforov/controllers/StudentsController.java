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

import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;

@Controller
@RequestMapping("/students")
public class StudentsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentsController.class);

    private static final String STUDENT_ATTR = "student";
    private static final String GROUP_ATTR = "group";
    private static final String GROUP_IN_ATTR = "groupIn";
    private static final String GROUPS_ATTR = "groups";
    private static final String STUDENTS_ATTR = "students";
    private static final String FIRST_NAME_ATTR = "firstName";
    private static final String LAST_NAME_ATTR = "lastName";

    private static final String VIEW_STUDENTS = "students/students";
    private static final String VIEW_TRANSFER_FORM = "students/transfer_form";
    private static final String VIEW_EDIT_FORM = "students/edit_form";
    private static final String VIEW_ADD_FORM = "students/add_student_form";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";

    private StudentsService studentService;

    private GroupService groupService;

    @Autowired
    public StudentsController(StudentsService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @ModelAttribute("group")
    public Group getGroup() {
        return new Group();
    }

    @GetMapping()
    public String showStudents(@RequestParam long groupId, Model model) {
        model.addAttribute(GROUP_IN_ATTR, groupService.getGroupById(groupId));
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        return VIEW_STUDENTS;
    }

    @GetMapping("/edit")
    public String edit(@RequestParam long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute(STUDENT_ATTR, student);
        return VIEW_EDIT_FORM;
    }

    @PostMapping("/edit")
    public String processEdit(@RequestParam long id, @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam long groupId, Model model) {
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        Group group = groupService.getGroupById(groupId);
        model.addAttribute(GROUP_IN_ATTR, group);
        try {
            studentService.updateStudent(new Student(id, firstName, lastName, groupId));
            model.addAttribute(SUCCESS_MSG, String.format("Student %s %s is successfully edited", firstName, lastName));

        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG, String.format("Warning! Cannot add Student %s %s in group %s! Alraedy exists!",
                    firstName, lastName, group.getGroupName()));
            return VIEW_STUDENTS;
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, String.format("Failed to edit student '%s' '%s'", firstName, lastName));
            return VIEW_STUDENTS;
        }
        model.addAttribute(GROUP_IN_ATTR, groupService.getGroupById(groupId));
        return VIEW_STUDENTS;
    }

    @GetMapping("/transfer")
    public String transfer(@RequestParam long id, String message, Model model) {
        Student student = studentService.getStudentById(id);
        Group group = groupService.getGroupByStudentId(id);
        List<Group> groups = groupService.getAllGroups();
        groups.remove(group);
        model.addAttribute(FAIL_MSG, message);
        model.addAttribute(STUDENT_ATTR, student);
        model.addAttribute(GROUP_ATTR, group);
        model.addAttribute(GROUPS_ATTR, groups);
        return VIEW_TRANSFER_FORM;
    }

    @PostMapping("/transfer")
    public String processTransfer(@RequestParam long studentId, @RequestParam String firstName,
            @RequestParam String lastName, @RequestParam long groupToId, @RequestParam String groupName, Model model) {
        if (groupToId == 0) {
            return "redirect:/students/transfer/?id=" + studentId + "&message=Choose group from list";
        }
        Group groupFrom = groupService.getGroupByName(groupName);
        Group groupTo = groupService.getGroupById(groupToId);
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute(GROUP_IN_ATTR, groupTo);
        model.addAttribute(GROUPS_ATTR, groups);
        try {
            studentService.transferStudent(studentId, groupToId);
            model.addAttribute(SUCCESS_MSG, String.format("Student %s %s was transferd successfully to group %s",
                    firstName, lastName, groupTo.getGroupName()));
        } catch (DataOperationException e) {
            model.addAttribute(GROUP_IN_ATTR, groupFrom);
            model.addAttribute(FAIL_MSG, String.format("Failed to transfer Student %s %s from group %s to group %s",
                    firstName, lastName, groupName, groupTo.getGroupName()));
            return VIEW_STUDENTS;
        }
        groups.remove(groupFrom);
        model.addAttribute(GROUP_IN_ATTR, groupService.getGroupById(groupToId));
        return VIEW_STUDENTS;
    }

    @GetMapping("/delete")
    public String deleteStudent(@RequestParam long id, Model model) {
        Group group = groupService.getGroupByStudentId(id);
        Student student = group.getStudentById(id);
        try {
            studentService.deleteStudentById(id);
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            model.addAttribute(GROUP_IN_ATTR, groupService.getGroupById(student.getGroupId()));
            model.addAttribute(SUCCESS_MSG, String.format("Student %s %s was deleted successfully from group %s",
                    student.getFirstName(), student.getLastName(), group.getGroupName()));
        } catch (DataOperationException e) {
            model.addAttribute(GROUP_IN_ATTR, group);
            model.addAttribute(FAIL_MSG, String.format("Failed to delete Student %s %s from group %s",
                    student.getFirstName(), student.getLastName(), group.getGroupName()));
            return VIEW_STUDENTS;
        }
        return VIEW_STUDENTS;
    }

    @PostMapping("/add")
    public String processAdding(@RequestParam String firstName, @RequestParam String lastName,
            @RequestParam long groupId, Model model) {
        Group group = groupService.getGroupById(groupId);
        model.addAttribute(GROUP_IN_ATTR, group);
        try {
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            if (groupId == 0) {
                model.addAttribute(FIRST_NAME_ATTR, firstName);
                model.addAttribute(LAST_NAME_ATTR, lastName);
                model.addAttribute(FAIL_MSG, "Choose group name for student!");
                return VIEW_STUDENTS;
            }
            studentService.addStudent(firstName, lastName, groupId);
            model.addAttribute(GROUP_IN_ATTR, groupService.getGroupById(groupId));
            model.addAttribute(SUCCESS_MSG, String.format("Student %s %s successfully added to group %s", firstName,
                    lastName, group.getGroupName()));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG, String.format("Warning! Cannot add Student %s %s in group %s! Alraedy exists!",
                    firstName, lastName, group.getGroupName()));
            return VIEW_STUDENTS;
        }
        return VIEW_STUDENTS;
    }

}
