package ua.com.nikiforov.rest_controllers;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.services.room.RoomService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomsRestController {

    private RoomService roomService;

    @Autowired
    public RoomsRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @ApiOperation(value = "Retrieves all existing Rooms")
    public List<RoomDTO> getAll() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    @ApiOperation(value = "Finds Room by id",
            notes = "Provide an id to look up specific room",
            response = RoomDTO.class
    )
    public RoomDTO getRoomDTO(@ApiParam(value = "ID value for Room to retrieve") @PathVariable int roomId) {
        return roomService.getRoomById(roomId);
    }

    @PostMapping
    @ApiOperation(value = "Adds Room to database",
            notes = "Provide room number and seats number for room",
            response = RoomDTO.class
    )
    public RoomDTO addRoomDTO(@ApiParam(value = "RoomDTO object without id to add to DB") @Valid @RequestBody RoomDTO roomDTO) {
        return roomService.addRoom(roomDTO);
    }

    @PutMapping("/{roomId}")
    @ApiOperation(value = "Updates existing Room",
            notes = "Provide room id and changed RoomDTO Request Body",
            response = RoomDTO.class
    )
    public RoomDTO updateRoom(@ApiParam(value = "ID value for Room to update") @PathVariable int roomId,
                              @ApiParam(value = "Changed RoomDTO to update") @Valid @RequestBody RoomDTO roomDTO) {
        roomDTO.setId(roomId);
        return roomService.updateRoom(roomDTO);
    }

    @DeleteMapping("/{roomId}")
    @ApiOperation(value = "Deletes Room from database",
            notes = "Provide room id to delete from DB"
    )
    public void deleteRoom(@ApiParam(value = "ID value for Room to delete") @PathVariable int roomId) {
        roomService.deleteRoomById(roomId);
    }

}
