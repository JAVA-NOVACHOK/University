package ua.com.nikiforov.controllers;


import java.util.ArrayList;
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
    
    @Autowired
    private RoomService roomService;

//  @GetMapping("/")
//  public String rooms() {
//     
//      return "rooms";
//  }
  @GetMapping("/show")
  public String roomsShow(Model model) {
      List<Room> rooms = roomService.getAllRooms();
      model.addAttribute("rooms",rooms);
      return "rooms";
  }

}
