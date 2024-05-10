package at.ac.tuwien.sepr.groupphase.backend.repository;


import at.ac.tuwien.sepr.groupphase.backend.entity.RecipeStep;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class RecipeStepRepository{

    private final String tablename = "RECIPE_STEP";
    private final String tablenamedesc = "RECIPE_DESCRIPTION_STEP";
    private final String tablenamerec = "RECIPE_RECIPE_STEP";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveAll(List<RecipeStep> steps) {
        for (RecipeStep step : steps) {
            BigInteger recid = null;
            BigInteger descid = null;

            if(step.getStepDescription() != null && step.getStepRecipe() == null){
                entityManager.createNativeQuery("INSERT INTO RECIPE_DESCRIPTION_STEP (NAME,DESCRIPTION) VALUES (?,?) RETURNING IDENTITY()",BigInteger.class)
                    .setParameter(1, step.getStepDescription().getName())
                    .setParameter(2, step.getStepDescription().getDescription())
                    .executeUpdate();

            }
            else {
                entityManager.createNativeQuery("INSERT INTO RECIPE_RECIPE_STEP (NAME,RECIPE_ID) VALUES (?,?) RETURNING IDENTITY()",BigInteger.class)
                    .setParameter(1, step.getStepRecipe().getName())
                    .setParameter(2, step.getStepRecipe().getRecipeId())
                    .executeUpdate();
            }



            entityManager.createNativeQuery(String.format("INSERT INTO %s (NAME,RECIPE_ID,STEP_NUMBER,STEP_DESCRIPTION_ID,STEP_RECIPE_ID) VALUES (?,?,?,?)", tablename))
                .setParameter(1,step.getName())
                .setParameter(2,step.getRecipeId())
                .setParameter(3,step.getStepNumber())
                .setParameter(4,descid)
                .setParameter(5,recid);

        }


    }

}