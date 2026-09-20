package nz.ac.aut.community_centre_booking.controller;

import nz.ac.aut.community_centre_booking.model.Room;
import nz.ac.aut.community_centre_booking.service.RoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// controller for handling room-related requests
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // retrieves all rooms and returns them as a list
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }
}