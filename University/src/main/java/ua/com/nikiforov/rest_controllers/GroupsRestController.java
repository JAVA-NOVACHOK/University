package ua.com.nikiforov.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.services.group.GroupService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupsRestController {

    private GroupService groupService;

    @Autowired
    public GroupsRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<GroupDTO> getGroups(){
        return groupService.getAllGroups();
    }

    @GetMapping("/{groupId}")
    public GroupDTO getGroup(@PathVariable long groupId){
        return groupService.getGroupById(groupId);
    }

    @PostMapping
    public GroupDTO addGroup(@Valid @RequestBody GroupDTO groupDTO){
        return groupService.addGroup(groupDTO);
    }

    @PutMapping("/{groupId}")
    public GroupDTO updateGroup(@PathVariable long groupId, @Valid @RequestBody GroupDTO groupDTO){
        groupDTO.setGroupId(groupId);
        return groupService.updateGroup(groupDTO);
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable long groupId){
        groupService.deleteGroup(groupId);
    }
}
