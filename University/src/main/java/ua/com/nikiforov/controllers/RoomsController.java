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

import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.room.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomsController {

    private static final String ROOM_ATTR = "rooms";
    private static final String VIEW_ROOMS = "rooms/rooms";
    private static final String VIEW_ADD_ROOM = "rooms/add_room_form";
    private static final String REDIRECT_ROOMS = "redirect:/rooms";
    private static final String MODEL_ATTR_ROOM = "room";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private RoomService roomService;

    private StudentsService studentService;

    @ModelAttribute(MODEL_ATTR_ROOM)
    public Room getRoom() {
        return new Room();
    }

    @Autowired
    public RoomsController(RoomService roomService, StudentsService studentService) {
        this.roomService = roomService;
        this.studentService = studentService;
    }

    @GetMapping()
    public String roomsShow(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute(ROOM_ATTR, rooms);
        return VIEW_ROOMS;
    }

    @GetMapping("/add")
    public String add() {
        return VIEW_ADD_ROOM;
    }

    @PostMapping("/add_room")
    public String addRoom(@RequestParam int roomNumber, @RequestParam int seatNumber, Model model) {
        try {
            roomService.addRoom(roomNumber, seatNumber);
            model.addAttribute(SUCCESS_MSG,
                    String.format("Room number '%d' with seats number '%d' added successfuly", roomNumber, seatNumber));
        } catch (DuplicateKeyException e) {
            model.addAttribute("message",
                    String.format("Cannot add room. Room with number '%d' already exists", roomNumber));
            return VIEW_ADD_ROOM;
        }
        model.addAttribute(ROOM_ATTR, roomService.getAllRooms());
        return VIEW_ROOMS;
    }

    @GetMapping("/delete")
    public String deleteRoom(@RequestParam int roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        try {
            roomService.deleteRoomById(roomId);
            model.addAttribute(SUCCESS_MSG, String.format("Room with '%d' deleted successfuly", room.getRoomNumber()));
        } catch (DataOperationException e) {
            model.addAttribute(FAIL_MSG, String.format("Failed to update room with number %d and seets number %d",
                    room.getRoomNumber(), room.getSeatNumber()));
        }
        model.addAttribute(ROOM_ATTR, roomService.getAllRooms());
        return VIEW_ROOMS;
    }

    @GetMapping("/edit")
    public String editRoom(@RequestParam int roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);
        return "rooms/edit_room_form";
    }

    @PostMapping("/edit_room")
    public String processEdit(@ModelAttribute(MODEL_ATTR_ROOM) Room room, Model model) {
        try {
            roomService.updateRoom(room.getRoomNumber(), room.getSeatNumber(), room.getId());
            model.addAttribute(SUCCESS_MSG,
                    String.format("Room number '%d' updated successfuly", room.getRoomNumber()));
        } catch (DuplicateKeyException e) {
            model.addAttribute("message", String.format(
                    "Warning! Failed to update room with number '%d'. It already exists!", room.getRoomNumber()));
        } catch (DataOperationException e) {
            model.addAttribute("message",
                    String.format("Error! Couldn't update room with number '%d'.", room.getRoomNumber()));
        }
        model.addAttribute(ROOM_ATTR, roomService.getAllRooms());
        return VIEW_ROOMS;
    }

}
