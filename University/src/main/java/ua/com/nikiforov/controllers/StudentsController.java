package ua.com.nikiforov.controllers;

import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;

@Controller
@RequestMapping("/students")
public class StudentsController {

    private static final Logger LOOGER = LoggerFactory.getLogger(StudentsController.class);
    private static final String GROUP_ATTR = "group";
    private static final String STUDENTS_ATTR = "students";
    private static final String VIEW_STUDENTS = "students/students";

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
    public String showStudents(@RequestParam long id, Model model) {
        Group group = groupService.getGroupById(id);
        List<Student> students = studentService.getStudentsByGroupId(id);
        Collections.sort(students);
        model.addAttribute(GROUP_ATTR, group);
        model.addAttribute(STUDENTS_ATTR, students);
        return VIEW_STUDENTS;
    }

    @GetMapping("/edit")
    public String edit(@RequestParam long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        LOOGER.debug("Get Edit. Student's groupId = {}", student.getGroupId());
        return "students/edit_form";
    }

    @PostMapping("/edit")
    public String processEdit(@RequestParam long id, @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam long groupId, Model model) {
        boolean actionResult = false;
        actionResult = studentService.updateStudent(new Student(id, firstName, lastName, groupId));
        if (actionResult) {
            model.addAttribute("success", String.format("Student %s %s is successfully edited", firstName, lastName));
        } else {
            model.addAttribute("failMessage", String.format("Failed to edit student '%s' '%s'", firstName, lastName));
            return "students/edit_form";
        }
        LOOGER.debug("Post Edit. Updated student's groupId = {}", groupId);
        Group group = groupService.getGroupById(groupId);
        List<Student> students = studentService.getStudentsByGroupId(groupId);
        model.addAttribute(GROUP_ATTR, group);
        model.addAttribute(STUDENTS_ATTR, students);
        return VIEW_STUDENTS;
    }

    @GetMapping("/transfer")
    public String transfer(@RequestParam long id, Model model) {
        Student student = studentService.getStudentById(id);
        Group group = groupService.getGroupByStudentId(id);
        List<Group> groups = groupService.getAllGroups();
        groups.remove(group);
        model.addAttribute("student", student);
        model.addAttribute("group", group);
        model.addAttribute("groups", groups);
        return "students/transfer_form";
    }

    @PostMapping("/transfer")
    public String processTransfer(
            @RequestParam long studentId, 
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam long groupToId,
            @RequestParam String groupName,
            Model model){
        boolean actionResult = false;
        actionResult = studentService.transferStudent(studentId, groupToId);
        Group groupTo = groupService.getGroupById(groupToId);
        if(!actionResult) {
            model.addAttribute("failMessage", 
                    String.format("Failed to transfer Student %s %s from group %s to group %s",
                            firstName,lastName,groupName,groupTo.getGroupName()));
            return "students/transfer_form";
        }
        List<Student> students = studentService.getStudentsByGroupId(groupToId);
        model.addAttribute(STUDENTS_ATTR, students);
        model.addAttribute("group", groupTo);
        model.addAttribute("success", 
                String.format("Student %s %s was transferd successfully to group %s", 
                        firstName,lastName,groupTo.getGroupName()));
        return VIEW_STUDENTS;
    }

}
