package nz.ac.aut.community_centre_booking.service;

import nz.ac.aut.community_centre_booking.model.Room;
import nz.ac.aut.community_centre_booking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}