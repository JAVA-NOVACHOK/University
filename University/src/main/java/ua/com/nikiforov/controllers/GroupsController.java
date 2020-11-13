package ua.com.nikiforov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private static final String GROUPS_ATTR = "groups";
    private static final String VIEW_GROUPS = "groups/groups";
    private static final String VIEW_ADD_GROUP = "groups/add_group_form";
    private static final String VIEW_EDIT_GROUP = "groups/edit_form";
    private static final String NEW_LINE = System.lineSeparator();
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

        List<Group> groups = groupService.getAllGroups();
        model.addAttribute(GROUPS_ATTR, groups);
        return VIEW_GROUPS;
    }

    @GetMapping("/add")
    public String add() {
        return VIEW_ADD_GROUP;
    }

    @PostMapping("/add_group")
    public String addGroup(@RequestParam String groupName, Model model) {
        try {
            groupService.addGroup(groupName);
            model.addAttribute(SUCCESS_MSG, String.format("Group with name '%s' was added successfully!", groupName));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot add group! Group with name '%s' already exists!", groupName));
            return VIEW_ADD_GROUP;
        }
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute(GROUPS_ATTR, groups);
        return VIEW_GROUPS;
    }

    @GetMapping("/delete")
    public String delete(Model model) {
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute(GROUPS_ATTR, groups);
        return "groups/delete_form";
    }

    @PostMapping("/delete")
    public String processingDelete(@RequestParam long groupId, Model model) {
        List<Student> students = studentsService.getStudentsByGroupId(groupId);
        Group group = groupService.getGroupById(groupId);
        String groupName = group.getGroupName();
        if (!students.isEmpty()) {
            model.addAttribute(GROUP, group);
            model.addAttribute("students", students);
            model.addAttribute(FAIL_MSG, String.format(
                    "Warning! Cannot delete group '%s'.%sReason: still has students in it!%sSolution: Remove or transfer all students.",
                    groupName, NEW_LINE, NEW_LINE));
            return "students";
        }
        if (groupService.deleteGroup(groupId)) {
            model.addAttribute(SUCCESS_MSG, "Successfully deleted group " + groupName);
        } else {
            model.addAttribute(FAIL_MSG, "Couldn't delete group " + groupName);
        }
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute(GROUPS_ATTR, groups);
        return VIEW_GROUPS;
    }

    @GetMapping("/edit")
    public String editGroup(@RequestParam long groupId, Model model) {
        Group group = groupService.getGroupById(groupId);
        model.addAttribute(GROUP, group);
        return VIEW_EDIT_GROUP;
    }

    @PostMapping("/edit")
    public String processEdit(@ModelAttribute(GROUP) Group group, Model model) {
        boolean actionResult = false;
        String groupName = group.getGroupName();
        try {
        actionResult = groupService.updateGroup(group.getId(), groupName);
        }catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot edit group. Group with name '%s' already exists", groupName));
            return VIEW_EDIT_GROUP;
        }
        if(!actionResult) {
            model.addAttribute(FAIL_MSG,String.format("Cannot edit group with name '%s'",groupName));
            return VIEW_EDIT_GROUP;
        }
        model.addAttribute(SUCCESS_MSG, "Successfully edited group " + groupName);
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute(GROUPS_ATTR, groups);
        return VIEW_GROUPS;
    }

}
