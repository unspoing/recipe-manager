package persistence;

import model.Recipe;
import model.Recipes;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestJsonWriter {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/meowwwwwwww\0meowmeowmeow:filename.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e){
            // pass
        }
    }

    @Test
    void testWriterEmptyRecipes() {
        try {
            Recipes recipes = new Recipes();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyRecipes.json");
            writer.open();
            writer.write(recipes);
            JsonReader reader = new JsonReader("./data/testWriterEmptyRecipes.json");
            recipes = reader.read();
            assertEquals(0, recipes.getNumOfRecipes());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralRecipes() {
        try {
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

            Recipes recipes = new Recipes();
            recipes.addRecipe(catRecipe);
            recipes.addRecipe(dogRecipe);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralRecipes.json");
            writer.open();
            writer.write(recipes);

            JsonReader reader = new JsonReader("./data/testWriterGeneralRecipes.json");
            Recipes readRecipes = reader.read();
            assertEquals(recipes.getNumOfRecipes(), 2);
            Recipe recipe1 = readRecipes.getRecipeWithIndex(0);
            assertEquals(recipe1.getName(), catRecipe.getName());
            assertEquals(recipe1.getIngredients(), catRecipe.getIngredients());
            assertEquals(recipe1.getTags(), catRecipe.getTags());
            assertEquals(recipe1.getSteps(), catRecipe.getSteps());
            Recipe recipe2 = readRecipes.getRecipeWithIndex(1);
            assertEquals(recipe2.getName(), dogRecipe.getName());
            assertEquals(recipe2.getIngredients(), dogRecipe.getIngredients());
            assertEquals(recipe2.getTags(), dogRecipe.getTags());
            assertEquals(recipe2.getSteps(), dogRecipe.getSteps());

        } catch (IOException e) {
        fail("exception should not have been thrown");
        }
    }
}