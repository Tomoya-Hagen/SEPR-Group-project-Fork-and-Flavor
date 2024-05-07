package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class RecipeStepDto {

    @NotNull(message = "stepnumber must not be null")
    int stepnumber;

    @NotNull(message = "name must not be null")
    String name;


    public int getStepnumber() {
        return stepnumber;
    }

    public void setStepnumber(int stepnumber) {
        this.stepnumber = stepnumber;
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
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        RecipeStepDto that = (RecipeStepDto) o;
        return stepnumber == that.stepnumber && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepnumber, name);
    }


}
