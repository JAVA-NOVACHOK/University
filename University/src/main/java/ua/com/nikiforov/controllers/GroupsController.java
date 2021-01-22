package ua.com.nikiforov.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.StudentsInGroupException;
import ua.com.nikiforov.services.group.GroupService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    Logger LOGGER = LoggerFactory.getLogger(GroupsController.class);

    private static final String GROUPS_ATTR = "groups";
    private static final String VIEW_GROUPS = "groups/groups";
    private static final String VIEW_STUDENTS = "students/students";
    private static final String VIEW_EDIT_GROUP = "groups/edit_group_form";
    private static final String VIEW_DELETE_GROUP = "groups/delete_group_form";
    private static final String GROUP_IN_ATTR = "groupIn";
    private static final String GROUP = "group";
    private static final String FAIL_MSG = "failMessage";
    private static final String SUCCESS_MSG = "success";

    private GroupService groupService;

    @Autowired
    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @ModelAttribute(GROUP)
    public GroupDTO getGroup() {
        return new GroupDTO();
    }

    @GetMapping()
    public String getGroups(Model model) {
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        return VIEW_GROUPS;
    }

    @PostMapping("/add")
    public String addGroup(@Valid @ModelAttribute GroupDTO groupDTO, Model model) {
        try {
            groupService.addGroup(groupDTO);
            model.addAttribute(SUCCESS_MSG, String.format("Group with name '%s' was added successfully!", groupDTO.getGroupName()));
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot add group! Group with name '%s' already exists!", groupDTO.getGroupName()));
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
        GroupDTO group = null;
        try {
            List<GroupDTO> groups = groupService.getAllGroups();
            model.addAttribute(GROUPS_ATTR, groups);
            group = groupService.getGroupById(groupId);
            groupService.deleteGroup(groupId);
            model.addAttribute(SUCCESS_MSG, "Successfully deleted group " + group.getGroupName());
            groups.remove(group);
            model.addAttribute(GROUPS_ATTR, groups);
        } catch (StudentsInGroupException e) {
            model.addAttribute(GROUP_IN_ATTR, group);
            model.addAttribute(FAIL_MSG, String.format(
                    "Warning! Cannot delete group '%s'.Reason: still has students in it! Solution: Remove or transfer all students.",
                    group.getGroupName()));
            return VIEW_STUDENTS;
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, "Error!Couldn't find group with id " + groupId);
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, "Error!Couldn't delete group. Try later.");
        }
        return VIEW_GROUPS;
    }

    @GetMapping("/edit")
    public String editGroup(@RequestParam long groupId, Model model) {
        try {
            model.addAttribute(GROUP, groupService.getGroupById(groupId));
        } catch (EntityNotFoundException e) {
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            model.addAttribute(FAIL_MSG, "Warning! Couldn't find group while editing!");
            return VIEW_GROUPS;
        } catch (DataOperationException e) {
            model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
            model.addAttribute(FAIL_MSG, "ERROR! Something went wrong while editing group!");
            return VIEW_GROUPS;
        }
        return VIEW_EDIT_GROUP;
    }

    @PostMapping("/edit")
    public String processEdit(@Valid @ModelAttribute(GROUP) GroupDTO group, Model model) {
        String groupName = group.getGroupName();
        try {
            groupService.updateGroup(group);
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot edit group. Group with name '%s' already exists", groupName));
            return VIEW_EDIT_GROUP;
        } catch (DataOperationException | EntityNotFoundException e) {
            e.printStackTrace();
            model.addAttribute(FAIL_MSG, String.format("Cannot edit group with name '%s'", groupName));
            return VIEW_EDIT_GROUP;
        }
        model.addAttribute(SUCCESS_MSG, "Successfully edited group " + groupName);
        model.addAttribute(GROUPS_ATTR, groupService.getAllGroups());
        return VIEW_GROUPS;
    }

}
