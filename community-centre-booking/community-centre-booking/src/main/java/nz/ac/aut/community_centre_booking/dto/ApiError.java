package nz.ac.aut.community_centre_booking.dto;

public record ApiError(
        String code,
        String message,
        String path) {
}