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
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private static final String GROUPS_ATTR = "groups";
    private static final String STUDENTS_ATTR = "students";
    private static final String VIEW_GROUPS = "groups/groups";
    private static final String VIEW_STUDENTS = "students/students";
    private static final String VIEW_ADD_GROUP = "groups/add_group_form";
    private static final String VIEW_EDIT_GROUP = "groups/edit_group_form";
    private static final String VIEW_DELETE_GROUP = "groups/delete_group_form";
    private static final String NEW_LINE = System.lineSeparator();
    private static final String GROUP_IN_ATTR = "groupIn";
    private static final String GROUP = "group";
    private static final String FAIL_MSG = "failMessage";
    private static final String SUCCESS_MSG = "success";

    private GroupService groupService;

    private StudentsService studentsService;

    @Autowired
    public GroupsController(GroupService groupService, StudentsService studentsService) {
        this.groupService = groupService;
        this.studentsService = studentsService;
    }

    @ModelAttribute(GROUP)
    public Group getGroup() {
        return new Group();
    }

    @GetMapping()
    public String getGroups(Model model) {
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        return VIEW_GROUPS;
    }

    @PostMapping("/add")
    public String addGroup(@RequestParam String groupName, Model model) {
        try {
            groupService.addGroup(groupName);
            model.addAttribute(SUCCESS_MSG, String.format("Group with name '%s' was added successfully!", groupName));
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot add group! Group with name '%s' already exists!", groupName));
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            return VIEW_GROUPS;
        }
        return VIEW_GROUPS;
    }

    @GetMapping("/delete")
    public String delete(Model model) {
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        return VIEW_DELETE_GROUP;
    }

    @PostMapping("/delete")
    public String processingDelete(@RequestParam long groupId, Model model) {
        try {
            List<Student> students = studentsService.getStudentsByGroupId(groupId);
            List<Group> groups = groupService.getAllGroups();
            model.addAttribute(GROUPS_ATTR, groups);
            Group group = groupService.getGroupById(groupId);
            String groupName = group.getGroupName();
            if (!students.isEmpty()) {
                model.addAttribute(GROUP_IN_ATTR, group);
                model.addAttribute(STUDENTS_ATTR, students);
                model.addAttribute(FAIL_MSG, String.format(
                        "Warning! Cannot delete group '%s'.%sReason: still has students in it!%sSolution: Remove or transfer all students.",
                        groupName, NEW_LINE, NEW_LINE));
                return VIEW_STUDENTS;
            }
            groupService.deleteGroup(groupId);
            model.addAttribute(SUCCESS_MSG, "Successfully deleted group " + groupName);
            groups.remove(group);
            model.addAttribute(GROUPS_ATTR, groups);
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, "Error!Couldn't finde group with id " + groupId);
            return VIEW_GROUPS;
        }catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, "Error!Couldn't delete group. Try later.");
            return VIEW_GROUPS;
        }
        return VIEW_GROUPS;
    }

    @GetMapping("/edit")
    public String editGroup(@RequestParam long groupId, Model model) {
        try {
        model.addAttribute(GROUP, groupService.getGroupById(groupId));
        }catch (EntityNotFoundException e) {
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            model.addAttribute(FAIL_MSG, "Warning! Couldn't find group while editing!");
            return VIEW_GROUPS;
        }catch (DataOperationException e) {
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            model.addAttribute(FAIL_MSG, "ERROR! Something went wrong while editing group!");
            return VIEW_GROUPS;
        }
        return VIEW_EDIT_GROUP;
    }

    @PostMapping("/edit")
    public String processEdit(@ModelAttribute(GROUP) Group group, Model model) {
        String groupName = group.getGroupName();
        try {
            groupService.updateGroup(group);
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot edit group. Group with name '%s' already exists", groupName));
            return VIEW_EDIT_GROUP;
        } catch (DataOperationException e) {
            e.printStackTrace();
            model.addAttribute(FAIL_MSG, String.format("Cannot edit group with name '%s'", groupName));
            return VIEW_EDIT_GROUP;
        }
        model.addAttribute(SUCCESS_MSG, "Successfully edited group " + groupName);
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        return VIEW_GROUPS;
    }

}
