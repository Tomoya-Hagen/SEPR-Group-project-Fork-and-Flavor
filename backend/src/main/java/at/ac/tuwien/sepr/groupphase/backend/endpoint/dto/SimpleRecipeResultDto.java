package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class SimpleRecipeResultDto {
    private Long recipeId;
    private String recipename;
    private boolean whichstep;


    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipename() {
        return recipename;
    }

    public void setRecipename(String recipename) {
        this.recipename = recipename;
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
        SimpleRecipeResultDto that = (SimpleRecipeResultDto) o;
        return whichstep == that.whichstep && Objects.equals(recipeId, that.recipeId) && Objects.equals(recipename, that.recipename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, recipename, whichstep);
    }
}
