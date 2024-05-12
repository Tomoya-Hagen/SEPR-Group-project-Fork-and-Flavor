package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class RecipeStepDescriptionDetailDto extends RecipeStepDetailDto {
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RecipeStepDescriptionDetailDto(long id,
                                          String name,
                                          int stepNumber,
                                          String description) {
        super(id, name, stepNumber);
        this.description = description;
    }

    public RecipeStepDescriptionDetailDto() {
        super();
    }
}
