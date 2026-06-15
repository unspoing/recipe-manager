package persistence;

import model.Recipe;
import model.Recipes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;

import org.json.*;

// represents a reader that reads recipe from json data stored in file
public class JsonReader {
    private String source;

    // constructs a reader that reads recipes from json data stored in file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads recipes from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Recipes read() throws IOException {
        String jsonData = readFile(source);
        JSONArray jsonObject = new JSONArray(jsonData);
        return parseRecipes(jsonObject);
    }
    
    // EFFECTS: reads source file as string and returns it
    public String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }


    // EFFECTS: gets recipe list based on json array provided
    public Recipes parseRecipes(JSONArray jsonArray) {
        Recipes recipes = new Recipes();
        for (Object jsonObject : jsonArray) {
            JSONObject recipe = (JSONObject) jsonObject;
            Recipe parsedRecipe = parseRecipe(recipe);
            recipes.addRecipe(parsedRecipe);
        }
        return recipes;
    }

    // EFFECTS: creates and returns recipe based on json object provided
    public Recipe parseRecipe(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        List<String> tags = parseStrings(jsonObject, "tags");
        List<String> ingredients = parseStrings(jsonObject, "ingredients");
        List<String> steps = parseStrings(jsonObject, "steps");
        Recipe recipe = new Recipe(name, tags, ingredients, steps);
        return recipe;
    }

    // EFFECTS: creates and returns a list of strings based on json object and name of list
    public List<String> parseStrings(JSONObject jsonObject, String descriptor) {
        JSONArray jsonArray = (JSONArray) jsonObject.get(descriptor);
        List<String> list = new ArrayList<>();
        for (Object item : jsonArray) {
            String word = (String) item;
            list.add(word);
        }
        return list;

    }
}