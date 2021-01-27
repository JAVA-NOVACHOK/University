package ua.com.nikiforov.rest_controllers;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.services.room.RoomService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomsRestController {

    private static final String ERROR_400 = "Request is wrong. Check for errors.";
    private static final String ERROR_404 = "Cannot find resource according to request";

    private static final int CODE_200 = 200;
    private static final int CODE_201 = 201;
    private static final int CODE_204 = 204;
    private static final int CODE_400 = 400;
    private static final int CODE_404 = 404;

    private RoomService roomService;

    @Autowired
    public RoomsRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @ApiOperation(value = "Retrieves all existing Rooms",
                responseContainer = "List",
                response = RoomDTO.class)
    @ApiResponses({
            @ApiResponse(code = CODE_200,message = "Successfully retrieved all rooms"),
            @ApiResponse(code = CODE_400,message = ERROR_400),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public List<RoomDTO> getAll() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    @ApiOperation(value = "Finds Room by id",
            notes = "Provide an id to look up specific room",
            response = RoomDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200,message = "Successfully retrieved room by ID"),
            @ApiResponse(code = CODE_400,message = ERROR_400),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public RoomDTO getRoomDTO(@ApiParam(value = "ID value for Room to retrieve") @PathVariable int roomId) {
        return roomService.getRoomById(roomId);
    }

    @PostMapping
    @ApiOperation(value = "Adds Room to database",
            notes = "Provide RoomDTO Request Body for room",
            response = RoomDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200,message = "Successfully accepted request"),
            @ApiResponse(code = CODE_201,message = "Successfully created room and inserted in DB"),
            @ApiResponse(code = CODE_400,message = ERROR_400),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public RoomDTO addRoomDTO(@ApiParam(value = "RoomDTO object with id=0 to add to DB",required = true) @Valid @RequestBody RoomDTO roomDTO) {
        return roomService.addRoom(roomDTO);
    }

    @PutMapping("/{roomId}")
    @ApiOperation(value = "Updates existing Room",
            notes = "Provide room id and changed RoomDTO Request Body",
            response = RoomDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200,message = "Successfully accepted request"),
            @ApiResponse(code = CODE_201,message = "Successfully updated room in DB"),
            @ApiResponse(code = CODE_400,message = ERROR_400),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public RoomDTO updateRoom(@ApiParam(value = "ID value for Room to update",required = true) @PathVariable int roomId,
                              @ApiParam(value = "Changed RoomDTO to update",required = true) @Valid @RequestBody RoomDTO roomDTO) {
        roomDTO.setId(roomId);
        return roomService.updateRoom(roomDTO);
    }

    @DeleteMapping("/{roomId}")
    @ApiOperation(value = "Deletes Room from database",
            notes = "Provide room id to delete from DB"
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200,message = "Successfully accepted request"),
            @ApiResponse(code = CODE_204,message = "Successfully deleted room from DB"),
            @ApiResponse(code = CODE_400,message = ERROR_400),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public void deleteRoom(@ApiParam(value = "ID value for Room to delete",required = true) @PathVariable int roomId) {
        roomService.deleteRoomById(roomId);
    }

}
