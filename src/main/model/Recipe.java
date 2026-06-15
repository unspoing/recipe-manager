package model;

import java.util.List;
import org.json.JSONObject;

// represents a recipe having a name, tags to describe it, and ingredients
public class Recipe {
    private String name; // name of the recipe
    private List<String> tags; // tags for the dish
    private List<String> ingredients; // ingredients for the dish
    private List<String> steps; // steps to make the dish

    // EFFECTS: sets values for recipe name, tags, and ingredients
    public Recipe(String name, List<String> tags, List<String> ingredients, List<String> steps) {
        this.name = name;
        this.tags = tags;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public List<String> getIngredients() {
        return this.ingredients;
    }

    public List<String> getSteps() {
        return this.steps;
    }

    // EFFECTS: returns true if tag exists in recipe
    public Boolean hasTag(String tag) {
        for (String descriptor : this.tags) {
            if (descriptor.equals(tag)) {
                return true;
            }
        }
        return false;
    }
    
    // EFFECTS: returns true if ingredient exists in recipe
    public Boolean hasIngredient(String ingredient) {
        for (String descriptor : this.ingredients) {
            if (descriptor == ingredient) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns a recipe in the form of a json
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("tags", tags);
        json.put("ingredients", ingredients);
        json.put("steps", steps);
        return json;
    }
}
