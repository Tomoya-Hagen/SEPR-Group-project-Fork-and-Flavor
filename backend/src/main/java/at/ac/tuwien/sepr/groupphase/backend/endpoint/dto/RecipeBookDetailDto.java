package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public record RecipeBookDetailDto(
    long id,
    String name,
    String description,
    long ownerId) {
}
