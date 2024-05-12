package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public record RecipeListDto(
    long id,
    String name,
    String description,
    long rating) {
}
