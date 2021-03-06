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

import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentsController.class);

    private static final String STUDENT_ATTR = "student";
    private static final String GROUP_ATTR = "group";
    private static final String GROUP_IN_ATTR = "groupIn";
    private static final String GROUPS_ATTR = "groups";
    private static final String FIRST_NAME_ATTR = "firstName";
    private static final String LAST_NAME_ATTR = "lastName";

    private static final String VIEW_STUDENTS = "students/students";
    private static final String VIEW_TRANSFER_FORM = "students/transfer_form";
    private static final String VIEW_EDIT_FORM = "students/edit_form";
    private static final String VIEW_GROUPS = "groups/groups";

    private static final String REDIRECT_TRANSFER = "redirect:/students/transfer/?id=";
    private static final String CHOOSE_GROUP_MSG = "&message=Choose group from list";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private static final String ERROR_MSG = "Error! Something went wrong. Failed to ";

    private StudentsService studentService;

    private GroupService groupService;

    @Autowired
    public StudentsController(StudentsService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @ModelAttribute("group")
    public GroupDTO getGroup() {
        return new GroupDTO();
    }

    @ModelAttribute(STUDENT_ATTR)
    public StudentDTO getStudent() {
        return new StudentDTO();
    }

    @GetMapping()
    public String showStudents(@RequestParam long groupId, Model model) {
        try {
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            model.addAttribute(GROUP_IN_ATTR, groupService.getGroupById(groupId));
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, String.format("%s find group by id %d.", ERROR_MSG, groupId));
            return VIEW_GROUPS;
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, String.format("%s show students!", ERROR_MSG));
            return VIEW_GROUPS;
        }
        return VIEW_STUDENTS;
    }

    @GetMapping("/edit")
    public String edit(@RequestParam long id, Model model) {
        try {
            StudentDTO student = studentService.getStudentById(id);
            model.addAttribute(STUDENT_ATTR, student);
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, "Warning! Couldn't find student.");
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, "ERROR! Failed to update student!");
        }
        return VIEW_EDIT_FORM;
    }

    @PostMapping("/edit")
    public String processEdit(@Valid @ModelAttribute(STUDENT_ATTR) StudentDTO studentDTO, Model model) {
        try {
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            GroupDTO groupDTO = groupService.getGroupById(studentDTO.getGroupId());
            model.addAttribute(GROUP_IN_ATTR, groupDTO);
            updateStudent(groupDTO, studentDTO, model);
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, String.format("Error! Something went wrong. Failed to edit student '%s' '%s'",
                    studentDTO.getFirstName(), studentDTO.getLastName()));
            return VIEW_STUDENTS;
        }
        model.addAttribute(GROUP_IN_ATTR, groupService.getGroupById(studentDTO.getGroupId()));
        return VIEW_STUDENTS;
    }

    @Transactional
     void updateStudent(GroupDTO groupDTO, StudentDTO studentDTO, Model model) {
        try {
            studentService.updateStudent(studentDTO);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Student %s %s is successfully edited", studentDTO.getFirstName(), studentDTO.getLastName()));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Warning! Cannot add Student %s %s in group %s! Alraedy exists!", studentDTO.getFirstName(),
                            studentDTO.getLastName(), groupDTO.getGroupName()));
        }
    }

    @GetMapping("/transfer")
    public String transfer(@RequestParam long id, String message, Model model) {
        try {
            StudentDTO student = studentService.getStudentById(id);
            GroupDTO group = groupService.getGroupById(student.getGroupId());
            List<GroupDTO> groups = groupService.getAllGroups();
            groups.remove(group);
            model.addAttribute(STUDENT_ATTR, student);
            model.addAttribute(GROUP_ATTR, group);
            model.addAttribute(GROUPS_ATTR, groups);
            model.addAttribute(FAIL_MSG, message);
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, String.format("Error! Couldn't get student by id %d!", id));
            return VIEW_STUDENTS;
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, "Error! Something went wrong while transfering!");
            return VIEW_STUDENTS;
        }
        return VIEW_TRANSFER_FORM;
    }

    @PostMapping("/transfer")
    public String processTransfer(@Valid @ModelAttribute(STUDENT_ATTR) StudentDTO student,
                                  @RequestParam long groupToId, Model model) {
        LOGGER.debug("GroupToId = {}", groupToId);
        long groupFromId = student.getGroupId();
        long studentId = student.getId();
        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        String groupName = student.getGroupName();
        if (groupToId == 0) {
            return REDIRECT_TRANSFER + studentId + CHOOSE_GROUP_MSG;
        }
        GroupDTO groupFrom = groupService.getGroupById(groupFromId);
        GroupDTO groupTo = groupService.getGroupById(groupToId);
        List<GroupDTO> groups = groupService.getAllGroups();
        model.addAttribute(GROUP_IN_ATTR, groupTo);
        try {
            studentService.transferStudent(studentId, groupToId);
            model.addAttribute(SUCCESS_MSG, String.format("Student %s %s was transferd successfully to group %s",
                    firstName, lastName, groupTo.getGroupName()));
            model.addAttribute(GROUP_IN_ATTR, groupService.getGroupById(groupToId));
        } catch (DuplicateKeyException e) {
            model.addAttribute(GROUP_IN_ATTR, groupFrom);
            model.addAttribute(FAIL_MSG, String.format("Failed to transfer Student %s %s from group %s to group %s. Such Student already exists.",
                    firstName, lastName, groupName, groupTo.getGroupName()));
        } catch (DataOperationException e) {
            model.addAttribute(GROUP_IN_ATTR, groupFrom);
            model.addAttribute(FAIL_MSG, String.format("Failed to transfer Student %s %s from group %s to group %s",
                    firstName, lastName, groupName, groupTo.getGroupName()));
        }
        model.addAttribute(GROUPS_ATTR, groups);
        return VIEW_STUDENTS;
    }


    @GetMapping("/delete")
    public String deleteStudent(@RequestParam long id, Model model) {
        StudentDTO student;
        GroupDTO group;
        try {
            student = studentService.getStudentById(id);
            group = groupService.getGroupById(student.getGroupId());
            studentService.deleteStudentById(id);
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            model.addAttribute(GROUP_IN_ATTR, groupService.getGroupById(student.getGroupId()));
            model.addAttribute(SUCCESS_MSG, String.format("Student %s %s was deleted successfully from group %s",
                    student.getFirstName(), student.getLastName(), group.getGroupName()));
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, String.format("Failed to delete Student by id %d! %s", id, e.getMessage()));
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            return VIEW_GROUPS;
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, String.format("Failed to delete Student by id %d", id));
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            return VIEW_GROUPS;
        }
        return VIEW_STUDENTS;
    }

    @PostMapping("/add")
    public String processAdding(@Valid @ModelAttribute(STUDENT_ATTR) StudentDTO student, Model model) {
        long groupId = student.getGroupId();
        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        GroupDTO group = groupService.getGroupById(groupId);
        model.addAttribute(GROUP_IN_ATTR, group);
        try {
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            if (groupId == 0) {
                model.addAttribute(FIRST_NAME_ATTR, firstName);
                model.addAttribute(LAST_NAME_ATTR, lastName);
                model.addAttribute(FAIL_MSG, "Choose group name for student!");
                return VIEW_STUDENTS;
            }
            studentService.addStudent(student);
            model.addAttribute(GROUP_IN_ATTR, groupService.getGroupById(groupId));
            model.addAttribute(SUCCESS_MSG, String.format("Student %s %s successfully added to group %s", firstName,
                    lastName, group.getGroupName()));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG, String.format("Warning! Cannot add Student %s %s in group %s! Already exists!",
                    firstName, lastName, group.getGroupName()));
            return VIEW_STUDENTS;
        }
        return VIEW_STUDENTS;
    }

}
