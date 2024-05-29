package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class RecipeCategoryDto {

    @NotNull(message = "categoryId must not be null")
    long id;

    public RecipeCategoryDto() {
    }


    public RecipeCategoryDto(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeCategoryDto that = (RecipeCategoryDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static final class RecipeCategoryDtoBuilder {
        private long id;

        private RecipeCategoryDtoBuilder() {
        }

        public static RecipeCategoryDtoBuilder aRecipeCategoryDto() {
            return new RecipeCategoryDtoBuilder();
        }


        public RecipeCategoryDtoBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public RecipeCategoryDto build() {
            RecipeCategoryDto recipeCategoryDto = new RecipeCategoryDto();
            recipeCategoryDto.setId(id);
            return recipeCategoryDto;
        }
    }
}
