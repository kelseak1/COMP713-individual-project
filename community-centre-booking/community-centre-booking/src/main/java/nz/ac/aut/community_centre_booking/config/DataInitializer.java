package nz.ac.aut.community_centre_booking.config;

import nz.ac.aut.community_centre_booking.model.Room;
import nz.ac.aut.community_centre_booking.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// adds initial room data to the database if no rooms exist
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoomRepository roomRepository;

    public DataInitializer(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) {
        // check if the room repository is empty and add initial rooms if so
        if (roomRepository.count() == 0) {
            roomRepository.save(
                    new Room(
                            "Main Hall",
                            120,
                            "Large room suitable for events and group activities."));

            roomRepository.save(
                    new Room(
                            "Meeting Room",
                            20,
                            "Smaller room suitable for meetings and workshops."));

            roomRepository.save(
                    new Room(
                            "Activity Room",
                            40,
                            "General-purpose room for classes and community activities."));
        }
    }
}