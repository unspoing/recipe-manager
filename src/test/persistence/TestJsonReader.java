package persistence;

import model.Recipe;
import model.Recipes;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestJsonReader {
    
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/MEOWWWWWWW.json");
        try {
            Recipes recipes = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test 
    void testReaderEmptyRecipes() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyRecipes.json");
        try {
            Recipes recipes = reader.read();
            assertEquals(0 , recipes.getNumOfRecipes());
            assertTrue(recipes.getRecipes().isEmpty());
        } catch (IOException e) {
            fail("couldnt read from file");
        }
    }

    @Test
    void testReaderGeneralRecipes() {
        List<String> tags = new ArrayList<>();
        tags.add("cat");
        tags.add("animal");
        List<String> ingredients = new ArrayList<>();
        ingredients.add("ears");
        ingredients.add("fur");
        List<String> steps = new ArrayList<>();
        steps.add("pet");
        steps.add("feed");
        Recipe catRecipe = new Recipe("meow",tags,ingredients,steps);

        List<String> tags2 = new ArrayList<>();
        tags2.add("dog");
        tags2.add("animal");
        List<String> ingredients2 = new ArrayList<>();
        ingredients2.add("fur");
        ingredients2.add("paws");
        List<String> steps2 = new ArrayList<>();
        steps2.add("walk");
        steps2.add("feed");
        Recipe dogRecipe = new Recipe("bark",tags2,ingredients2,steps2);

        JsonReader reader = new JsonReader("./data/testReaderGeneralRecipes.json");
        try {
            Recipes recipes = reader.read();
            assertEquals(recipes.getNumOfRecipes(), 2);
            Recipe recipe1 = recipes.getRecipeWithIndex(0);
            assertEquals(recipe1.getName(), catRecipe.getName());
            assertEquals(recipe1.getIngredients(), catRecipe.getIngredients());
            assertEquals(recipe1.getTags(), catRecipe.getTags());
            assertEquals(recipe1.getSteps(), catRecipe.getSteps());
            Recipe recipe2 = recipes.getRecipeWithIndex(1);
            assertEquals(recipe2.getName(), dogRecipe.getName());
            assertEquals(recipe2.getIngredients(), dogRecipe.getIngredients());
            assertEquals(recipe2.getTags(), dogRecipe.getTags());
            assertEquals(recipe2.getSteps(), dogRecipe.getSteps());
        } catch (IOException e) {
            fail("couldnt read from file");
        }
    }
}
