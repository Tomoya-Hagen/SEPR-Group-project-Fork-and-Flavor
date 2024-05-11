package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class RecipeStepDescriptionDetailDto extends RecipeStepDetailDto {
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
