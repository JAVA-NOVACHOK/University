package ua.com.nikiforov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.services.room.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomsController {

    private static final String ROOMS_ATTR = "rooms";
    private static final String VIEW_ROOMS = "rooms/rooms";
    private static final String VIEW_EDIT_ROOM = "rooms/edit_room_form";
    private static final String MODEL_ATTR_ROOM = "room";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";

    private RoomService roomService;

    @ModelAttribute(MODEL_ATTR_ROOM)
    public RoomDTO getRoom() {
        return new RoomDTO();
    }

    @Autowired
    public RoomsController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping()
    public String roomsShow(Model model) {
        List<RoomDTO> rooms = roomService.getAllRooms();
        model.addAttribute(ROOMS_ATTR, rooms);
        return VIEW_ROOMS;
    }

    @PostMapping("/add")
    public String addRoom(@ModelAttribute(MODEL_ATTR_ROOM) RoomDTO roomDTO, Model model) {
        try {
            roomService.addRoom(roomDTO);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Room number '%d' with seats number '%d' added successfuly", roomDTO.getRoomNumber(), roomDTO.getSeatNumber()));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG,
                    String.format("Cannot add room. Room with number '%d' already exists", roomDTO.getRoomNumber()));
        }
        model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
        return VIEW_ROOMS;
    }

    @GetMapping("/delete")
    public String deleteRoom(@RequestParam int id, Model model) {
        try {
            roomService.deleteRoomById(id);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Room with '%d' deleted successfuly", id));
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, String.format("Failed to delete room with number and seets number %d",
                   id));
        }
        model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
        return VIEW_ROOMS;
    }

    @GetMapping("/edit")
    public String editRoom(@RequestParam int roomId, Model model) {
        try {
            RoomDTO room = roomService.getRoomById(roomId);
            model.addAttribute("room", room);
        } catch (EntityNotFoundException e) {
            model.addAttribute(FAIL_MSG, String.format("Warning! Couln't find room with id %d ", roomId));
            model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
            return VIEW_ROOMS;
        }
        return VIEW_EDIT_ROOM;
    }

    @PostMapping("/edit")
    public String processEdit(@ModelAttribute(MODEL_ATTR_ROOM) RoomDTO room, Model model) {
        try {
            roomService.updateRoom(room);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Room number '%d' updated successfuly", room.getRoomNumber()));
        } catch (DuplicateKeyException e) {
            model.addAttribute(FAIL_MSG, String.format(
                    "Warning! Failed to update room with number '%d'. It already exists!", room.getRoomNumber()));
        }
        model.addAttribute(ROOMS_ATTR, roomService.getAllRooms());
        return VIEW_ROOMS;
    }

}
