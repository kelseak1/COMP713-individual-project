package nz.ac.aut.community_centre_booking.repository;

import nz.ac.aut.community_centre_booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

// repository for Room entity
public interface RoomRepository extends JpaRepository<Room, Long> {
}