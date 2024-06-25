package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecommendEvaluation;
import at.ac.tuwien.sepr.groupphase.backend.entity.Ingredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimilarityUtils {

    public static double calculateDistance(List<RecommendEvaluation> list1, List<RecommendEvaluation> list2) {
        // Create maps from lists for easy lookup
        Map<Ingredient, Float> map1 = createEvaluationMap(list1);
        Map<Ingredient, Float> map2 = createEvaluationMap(list2);

        // Union of keys from both maps
        double sum = 0.0;
        for (Ingredient key : map1.keySet()) {
            double weight1 = map1.get(key);
            double weight2 = map2.getOrDefault(key, 0.0f);
            sum += Math.pow(weight1 - weight2, 2);
        }

        for (Ingredient key : map2.keySet()) {
            if (!map1.containsKey(key)) {
                double weight2 = map2.get(key);
                sum += Math.pow(weight2, 2);
            }
        }

        return Math.sqrt(sum);
    }

    private static Map<Ingredient, Float> createEvaluationMap(List<RecommendEvaluation> list) {
        Map<Ingredient, Float> map = new HashMap<>();
        for (RecommendEvaluation eval : list) {
            map.put(eval.getIngredient(), eval.getScore());
        }
        return map;
    }
}
