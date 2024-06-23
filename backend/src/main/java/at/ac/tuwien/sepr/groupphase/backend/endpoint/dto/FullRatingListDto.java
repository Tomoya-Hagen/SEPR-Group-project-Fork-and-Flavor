package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public record FullRatingListDto(
    UserDto user,
    double cost,
    double taste,
    double easeOfPrep,
    String review,
    long recipeId,
    String name
) {
}
