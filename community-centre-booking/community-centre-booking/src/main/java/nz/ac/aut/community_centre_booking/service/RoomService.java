package nz.ac.aut.community_centre_booking.service;

import nz.ac.aut.community_centre_booking.model.Room;
import nz.ac.aut.community_centre_booking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// handles room logic between the controller and repository
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // retrieves all rooms from the repository
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}