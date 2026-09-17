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

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

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
}