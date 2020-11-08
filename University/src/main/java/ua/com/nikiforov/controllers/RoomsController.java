package ua.com.nikiforov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.services.room.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomsController {
    
    private static final String MAPPING_SLASH = "/";
    private static final String ROOM_ATTR = "rooms";
    private static final String VIEW_ROOMS= "rooms";

    private RoomService roomService;

    @Autowired
    public RoomsController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(MAPPING_SLASH)
    public String roomsShow(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute(ROOM_ATTR, rooms);
        return VIEW_ROOMS;
    }

}
