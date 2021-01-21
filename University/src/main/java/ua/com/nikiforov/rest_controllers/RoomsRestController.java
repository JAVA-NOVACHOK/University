package ua.com.nikiforov.rest_controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.services.room.RoomService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest_rooms")
public class RoomsRestController {

    private RoomService roomService;

    @Autowired
    public RoomsRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomDTO> getAll(){
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    public RoomDTO getRoomDTO(@PathVariable("roomId") int id){
        return roomService.getRoomById(id);
    }

    @PostMapping
    public RoomDTO addRoomDTO(@Valid @RequestBody RoomDTO roomDTO){
        return roomService.addRoom(roomDTO);
    }

    @PutMapping("/{roomId}")
    public RoomDTO updateRoom(@PathVariable("roomId") int roomId, @Valid @RequestBody RoomDTO roomDTO){
        roomDTO.setId(roomId);
        return roomService.updateRoom(roomDTO);
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable("roomId") int roomId){
        roomService.deleteRoomById(roomId);
    }

}
