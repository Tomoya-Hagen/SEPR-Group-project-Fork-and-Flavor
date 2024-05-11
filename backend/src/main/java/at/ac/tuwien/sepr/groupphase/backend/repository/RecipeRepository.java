package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Message;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecipeRepository{

    @PersistenceContext
    private EntityManager entityManager;

    public Long findMaxId(){
        return (Long) entityManager.createQuery("SELECT COALESCE(MAX(r.id), 0) FROM Recipe r").getSingleResult();
    }

    public Recipe finbyId(Long id){
        Recipe recipe = entityManager.createQuery("select a FROM Recipe a WHERE a.id = :id", Recipe.class).setParameter("id", id).getSingleResult();
        return recipe;
    }

    public Recipe finbyId2(Long id){
        Recipe recipe = entityManager.createQuery("select a FROM Recipe a JOIN FETCH a.categories WHERE a.id = :id", Recipe.class).setParameter("id", id).getSingleResult();
        return recipe;
    }

    @Transactional
    public long save(Recipe recipe){
        entityManager.persist(recipe);
        return recipe.getId();
    }
}
