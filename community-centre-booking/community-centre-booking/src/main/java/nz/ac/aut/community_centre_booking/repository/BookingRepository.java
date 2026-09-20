package nz.ac.aut.community_centre_booking.repository;

import nz.ac.aut.community_centre_booking.model.Booking;
import nz.ac.aut.community_centre_booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

// repository for Booking entity
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // find bookings by room and booking date
    List<Booking> findByRoomAndBookingDate(
            Room room,
            LocalDate bookingDate);
}