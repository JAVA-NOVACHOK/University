package ua.com.nikiforov.rest_controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.services.group.GroupService;

import javax.validation.Valid;
import java.util.List;

import static ua.com.nikiforov.error_holder.ErrorMessage.*;

@RestController
@RequestMapping("/api/groups")
public class GroupsRestController {
    private GroupService groupService;

    @Autowired
    public GroupsRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    @ApiOperation(
            value = "Retrieves all existing groups",
            responseContainer = "List",
            response = GroupDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully retrieved all groups"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public List<GroupDTO> getGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{groupId}")
    @ApiOperation(
            value = "Retrieves group by ID",
            notes = "Provide ID of the group to retrieve",
            response = GroupDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully retrieved group by ID"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public GroupDTO getGroup(@ApiParam(value = "ID of the group to find", required = true) @PathVariable long groupId) {
        return groupService.getGroupById(groupId);
    }

    @PostMapping
    @ApiOperation(
            value = "Adds new group to university",
            notes = "Provide GroupDTO Request Body to record group to university",
            response = GroupDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully added group to university"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public GroupDTO addGroup(@ApiParam(value = "GroupDTO object with id=0 to add group to university", required = true)
                             @Valid @RequestBody GroupDTO groupDTO) {
        return groupService.addGroup(groupDTO);
    }

    @PutMapping("/{groupId}")
    @ApiOperation(
            value = "Updates existing group",
            notes = "Provide GroupDTO request body with id = 0 to update",
            response = GroupDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully updated group in university"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public GroupDTO updateGroup(@ApiParam(value = "ID of the group to update", required = true) @PathVariable long groupId,
                                @ApiParam(value = "Updated GroupDTO request body", required = true) @Valid @RequestBody GroupDTO groupDTO) {
        groupDTO.setGroupId(groupId);
        return groupService.updateGroup(groupDTO);
    }

    @DeleteMapping("/{groupId}")
    @ApiOperation(
            value = "Deletes group from university",
            notes = "Provide ID to delete group"
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully deleted group from university"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public void deleteGroup(@ApiParam(value = "ID of the group to delete", required = true) @PathVariable long groupId) {
        groupService.deleteGroup(groupId);
    }
}
