package ua.com.nikiforov.rest_controllers;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.services.room.RoomService;

import javax.validation.Valid;
import java.util.List;

import static ua.com.nikiforov.error_holder.ErrorMessage.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomsRestController {

    private RoomService roomService;

    @Autowired
    public RoomsRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @ApiOperation(value = "Retrieves all existing rooms",
            responseContainer = "List",
            response = RoomDTO.class)
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully retrieved all rooms"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public List<RoomDTO> getAll() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    @ApiOperation(value = "Finds room by id",
            notes = "Provide an ID to look up specific room",
            response = RoomDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully retrieved room by ID"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public RoomDTO getRoomDTO(@ApiParam(value = "ID value for Room to retrieve") @PathVariable int roomId) {
        return roomService.getRoomById(roomId);
    }

    @PostMapping
    @ApiOperation(value = "Adds new room to university",
            notes = "Provide RoomDTO Request Body to record room to university",
            response = RoomDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully created room and inserted in university"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public RoomDTO addRoomDTO(@ApiParam(value = "RoomDTO object with id=0 to add to university", required = true) @Valid @RequestBody RoomDTO roomDTO) {
        return roomService.addRoom(roomDTO);
    }

    @PutMapping("/{roomId}")
    @ApiOperation(value = "Updates existing room",
            notes = "Provide room id and changed RoomDTO Request Body",
            response = RoomDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully updated existing room"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public RoomDTO updateRoom(@ApiParam(value = "ID value for Room to update", required = true) @PathVariable int roomId,
                              @ApiParam(value = "Updated RoomDTO request body", required = true) @Valid @RequestBody RoomDTO roomDTO) {
        roomDTO.setId(roomId);
        return roomService.updateRoom(roomDTO);
    }

    @DeleteMapping("/{roomId}")
    @ApiOperation(value = "Deletes Room from database",
            notes = "Provide room id to delete from university"
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully deleted room from university"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public void deleteRoom(@ApiParam(value = "ID value for Room to delete", required = true) @PathVariable int roomId) {
        roomService.deleteRoomById(roomId);
    }

}
