package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.json.*;

public class TestRecipe {
    private String name; 
    private List<String> tags;
    private List<String> ingredients;
    private List<String> steps;
    private Recipe recipe;

    @BeforeEach
    void runBefore() {
        tags = new ArrayList<>();
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
            
        name = "meow";
        tags.add("cat");
        ingredients.add("paws");
        ingredients.add("fur");
        steps.add("feed");
        steps.add("pet");
        recipe = new Recipe(name, tags, ingredients, steps);
    }

    @Test
    void testConstructor() {
        assertEquals("meow",recipe.getName());
        assertEquals(tags,recipe.getTags());
        assertEquals(ingredients,recipe.getIngredients());
        assertEquals(steps,recipe.getSteps());
    }

    @Test
    void testHasTag() {
        assertTrue(recipe.hasTag("cat"));
        assertTrue(recipe.hasTag("cat"));
        assertFalse(recipe.hasTag("dog"));
    }

    @Test
    void testHasIngredient() {
        assertTrue(recipe.hasIngredient("paws"));
        assertTrue(recipe.hasIngredient("fur"));
        assertFalse(recipe.hasIngredient("liver"));
    }

    @Test
    void testToJson() {
        JSONObject jsonObject = recipe.toJson();
        assertEquals(jsonObject.get("name"), "meow");
        JSONArray jsonArrayTag = (JSONArray) jsonObject.get("tags");
        assertEquals(jsonArrayTag.getString(0), "cat");
        JSONArray jsonArrayIng = (JSONArray) jsonObject.get("ingredients");
        assertEquals(jsonArrayIng.getString(0), "paws");
        assertEquals(jsonArrayIng.getString(1), "fur");
        JSONArray jsonArraySteps = (JSONArray) jsonObject.get("steps");
        assertEquals(jsonArraySteps.getString(0), "feed");
        assertEquals(jsonArraySteps.getString(1), "pet");
    }

}
