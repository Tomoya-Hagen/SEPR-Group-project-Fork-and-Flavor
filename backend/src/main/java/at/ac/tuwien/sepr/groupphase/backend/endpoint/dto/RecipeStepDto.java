package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class RecipeStepDto {

    @NotNull(message = "name must not be null")
    String name;

    private String description;

    private long recipeId;

    @NotNull(message = "whichstep must not be null")
    private boolean whichstep;

    public RecipeStepDto() {
    }

    public RecipeStepDto(String name, String description, long recipeId, boolean whichstep) {
        this.name = name;
        this.description = description;
        this.recipeId = recipeId;
        this.whichstep = whichstep;
    }

    public boolean isCorrect() {
        return (whichstep && description != "" && recipeId == 0) || (!whichstep && description == "" && recipeId != 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
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

    public boolean isWhichstep() {
        return whichstep;
    }

    public void setWhichstep(boolean whichstep) {
        this.whichstep = whichstep;
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
        return recipeId == that.recipeId
            && whichstep == that.whichstep
            && Objects.equals(name, that.name)
            && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, recipeId, whichstep);
    }


    public static final class RecipeStepDtoBuilder {
        private String name;
        private String description;
        private long recipeId;
        private boolean whichstep;

        private RecipeStepDtoBuilder() {
        }

        public static RecipeStepDtoBuilder aRecipeStepDto() {
            return new RecipeStepDtoBuilder();
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

        public RecipeStepDtoBuilder withWhichstep(boolean whichstep) {
            this.whichstep = whichstep;
            return this;
        }

        public RecipeStepDto build() {
            RecipeStepDto recipeStepDto = new RecipeStepDto();
            recipeStepDto.setName(name);
            recipeStepDto.setDescription(description);
            recipeStepDto.setRecipeId(recipeId);
            recipeStepDto.setWhichstep(whichstep);
            return recipeStepDto;
        }
    }
}
