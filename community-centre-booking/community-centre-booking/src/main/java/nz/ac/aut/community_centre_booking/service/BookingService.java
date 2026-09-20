package nz.ac.aut.community_centre_booking.service;

import nz.ac.aut.community_centre_booking.dto.BookingRequest;
import nz.ac.aut.community_centre_booking.model.Booking;
import nz.ac.aut.community_centre_booking.model.Room;
import nz.ac.aut.community_centre_booking.repository.BookingRepository;
import nz.ac.aut.community_centre_booking.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// contains main booking rules and logic for creating and deleting bookings
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository) {

        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    // gets all current bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // creates a new booking with validation checks for room availability and time
    // conflicts
    @Transactional
    public Booking createBooking(BookingRequest request) {

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new NoSuchElementException("Room not found."));

        if (!request.startTime().isBefore(request.endTime())) {
            throw new IllegalArgumentException(
                    "End time must be after start time.");
        }

        List<Booking> existingBookings = bookingRepository.findByRoomAndBookingDate(
                room,
                request.bookingDate());

        for (Booking existingBooking : existingBookings) {
            // check for time overlap with existing bookings
            boolean overlaps = request.startTime().isBefore(existingBooking.getEndTime())
                    && request.endTime().isAfter(existingBooking.getStartTime());

            if (overlaps) {
                throw new IllegalStateException(
                        "The room is already booked during this time.");
            }
        }

        Booking booking = new Booking(
                request.customerName(),
                request.customerEmail(),
                request.purpose(),
                request.bookingDate(),
                request.startTime(),
                request.endTime(),
                room);

        return bookingRepository.save(booking);
    }

    // deletes a booking by its ID, throwing an exception if the booking does not
    // exist
    @Transactional
    public void deleteBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Booking not found."));

        bookingRepository.delete(booking);
    }
}