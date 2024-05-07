package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class RecipeCategoryDto {
    @NotNull(message = "categoryId must not be null")
    int categoryId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int id) {
        this.categoryId = id;
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
        return categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryId);
    }


    public static final class RecipeCategoryDtoBuilder {
        private int categoryId;

        private RecipeCategoryDtoBuilder() {
        }

        public static RecipeCategoryDtoBuilder aRecipeCategoryDto() {
            return new RecipeCategoryDtoBuilder();
        }

        public RecipeCategoryDtoBuilder withId(int categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public RecipeCategoryDto build() {
            RecipeCategoryDto recipeCategoryDto = new RecipeCategoryDto();
            recipeCategoryDto.setCategoryId(categoryId);
            return recipeCategoryDto;
        }
    }
}
