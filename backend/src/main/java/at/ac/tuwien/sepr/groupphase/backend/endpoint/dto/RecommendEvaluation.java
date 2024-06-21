package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;

public class RecommendEvaluation {

    Ingredient ingredient;
    int score;
    int multiplicator;

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMultiplicator() {
        return multiplicator;
    }

    public void setMultiplicator(int multiplicator) {
        this.multiplicator = multiplicator;
    }
}
