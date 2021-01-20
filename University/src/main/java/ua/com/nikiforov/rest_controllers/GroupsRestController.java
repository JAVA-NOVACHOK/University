package ua.com.nikiforov.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.services.group.GroupService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest_groups")
public class GroupsRestController {

    private static final String GROUP_ID = "groupId";

    private GroupService groupService;

    @Autowired
    public GroupsRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping()
    public List<GroupDTO> getGroups(){
        return groupService.getAllGroups();
    }

    @GetMapping("/{groupId}")
    public GroupDTO getGroup(@PathVariable(GROUP_ID) long groupId){
        return groupService.getGroupById(groupId);
    }

    @PostMapping()
    public GroupDTO addGroup(@Valid @RequestBody GroupDTO groupDTO){
        return groupService.addGroup(groupDTO);
    }

    @PutMapping("/{groupId}")
    public GroupDTO updateGroup(@PathVariable(GROUP_ID) long groupId, @Valid @RequestBody GroupDTO groupDTO){
        groupDTO.setGroupId(groupId);
        return groupService.updateGroup(groupDTO);
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable(GROUP_ID) long groupId){
        groupService.deleteGroup(groupId);
    }
}
