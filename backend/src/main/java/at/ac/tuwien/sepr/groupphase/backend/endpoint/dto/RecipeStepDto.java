package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class RecipeStepDto {

    @NotNull(message = "stepnumber must not be null")
    int stepNumber;

    @NotNull(message = "name must not be null")
    String name;

    private String description;

    private long recipeId;

    @NotNull(message = "whichstep must not be null")
    private boolean whichstep;

    @NotNull(message = "name must not be null")
    private String recipename;

    public String getDescription() {
        return description;
    }

    public boolean isWhichstep() {
        return whichstep;
    }

    public void setWhichstep(boolean whichstep) {
        this.whichstep = whichstep;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipename() {
        return recipename;
    }

    public void setRecipename(String recipename) {
        this.recipename = recipename;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public boolean isCorrect(){
        if((whichstep && description != null && recipeId == 0) || (!whichstep && description == null && recipeId != 0)){
            return true;
        }
        return false;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeStepDto that = (RecipeStepDto) o;
        return stepNumber == that.stepNumber
            && recipeId == that.recipeId
            && Objects.equals(name, that.name)
            && Objects.equals(description, that.description)
            && Objects.equals(recipename, that.recipename)
            && Objects.equals(whichstep, that.whichstep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepNumber, name, description, recipeId, recipename, whichstep);
    }


    public static final class RecipeStepDtoBuilder {
        private  int stepNumber;
        private String name;
        private String description;
        private long recipeId;
        private String recipename;
        private boolean whichstep;

        private RecipeStepDtoBuilder() {
        }

        public static RecipeStepDtoBuilder aRecipeStepDto() {
            return new RecipeStepDtoBuilder();
        }

        public RecipeStepDtoBuilder withStepNumber(int stepNumber) {
            this.stepNumber = stepNumber;
            return this;
        }

        public RecipeStepDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeStepDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeStepDtoBuilder withRecipeId(long recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public RecipeStepDtoBuilder withRecipename(String recipename) {
            this.recipename = recipename;
            return this;
        }

        public RecipeStepDtoBuilder withWhichstep(boolean whichstep) {
            this.whichstep = whichstep;
            return this;
        }

        public RecipeStepDto build() {
            RecipeStepDto recipeStepDto = new RecipeStepDto();
            recipeStepDto.setStepNumber(stepNumber);
            recipeStepDto.setName(name);
            recipeStepDto.setDescription(description);
            recipeStepDto.setRecipeId(recipeId);
            recipeStepDto.setRecipename(recipename);
            recipeStepDto.setWhichstep(whichstep);
            return recipeStepDto;
        }
    }
}
