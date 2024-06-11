package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public record AllergenDetailDto(
    long id,
    String name,
    String description,
    String type
) {
}
