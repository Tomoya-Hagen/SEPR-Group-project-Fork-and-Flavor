package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public record RatingCreateDto(
    long recipeId,
    long cost,
    long taste,
    long easeOfPrep,
    String review
) {
}
