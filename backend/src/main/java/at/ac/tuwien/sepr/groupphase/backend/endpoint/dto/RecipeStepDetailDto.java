package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class RecipeStepDetailDto {
    private long id;
    private String name;
    private int stepNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
