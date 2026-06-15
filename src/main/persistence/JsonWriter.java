package persistence;

import model.Recipe;
import model.Recipes;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

// Represents a writer that writes JSON representation of recipes to file
public class JsonWriter {
    private static final int TAB = 4;
    private String destination;
    private PrintWriter writer;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file 
    // cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of recipes to file
    public void write(Recipes recipes) {
        JSONArray json = getJsonArray(recipes.getRecipes());
        writer.print(json.toString(TAB));
        writer.close();
    }

    // EFFECTS: turns list of recipes into an array of jsons
    public JSONArray getJsonArray(List<Recipe> recipes) {
        JSONArray json = new JSONArray();
        for (Recipe recipe : recipes) {
            JSONObject recipeJson = recipe.toJson();
            json.put(recipeJson);
        }
        return json;
    }

}
