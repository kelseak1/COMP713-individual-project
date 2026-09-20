package nz.ac.aut.community_centre_booking.dto;

// DTO for API error response, structure for errors
public record ApiError(
        String code,
        String message,
        String path) {
}