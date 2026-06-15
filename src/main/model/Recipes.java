package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

// represents a collection of recipes
public class Recipes {
    private List<Recipe> recipes; // list of recipes
    private int recipeNum;
    private EventLog log;

    // EFFECTS: sets values for recipe name, tags, and ingredients
    public Recipes() {
        this.recipes = new ArrayList<>();
        this.recipeNum = 0;
        this.log = EventLog.getInstance();
    }

    public List<Recipe> getRecipes() {
        return this.recipes;
    }

    // REQUIRES: index < recipes size
    // EFFECTS: returns whatever recipe is in the index of recipe collection
    public Recipe getRecipeWithIndex(int index) {
        return this.recipes.get(index);
    }

    // EFFECTS: returns recipe with given name if found, else returns null
    public Recipe getRecipeWithname(String name) {
        for (Recipe recipe : this.recipes) {
            if (recipe.getName().equals(name)) {
                return recipe;
            }
        }
        return null;
    }

    public int getNumOfRecipes() {
        return this.recipeNum;
    }

    // MODIFIES: this
    // EFFECTS: adds a recipe to collection and increases recipe number
    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        this.recipeNum += 1;
        Event event = new Event("added recipe to recipes");
        log.logEvent(event);

    }

    // EFFECTS: returns a list of all recipes with specific tag
    public List<Recipe> getAllWithTag(String tag) {
        List<Recipe> listOfRecipes = new ArrayList<>();
        for (Recipe recipe : this.recipes) {
            if (recipe.hasTag(tag)) {
                listOfRecipes.add(recipe);
            }
        }
        return listOfRecipes;
    }

    // EFFECTS: returns a list of all recipes with specific ingredient
    public List<Recipe> getAllWithIngredient(String ingredient) {
        List<Recipe> listOfRecipes = new ArrayList<>();
        for (Recipe recipe : this.recipes) {
            if (recipe.hasIngredient(ingredient)) {
                listOfRecipes.add(recipe);
            }
        }
        return listOfRecipes;
    }

    
    // EFFECTS: given a collection of recipes and tags, returns a dictionary with
    // keys as recipes and values as number of times matching tags are seen in the recipe
    public HashMap<Recipe, Integer> getTagsPerRecipe(Recipes recipes, List<String> tags) {
        HashMap<Recipe, Integer> recMap = new HashMap<>();
        for (String descriptor : tags) {
            List<Recipe> matchingRecipes = recipes.getAllWithTag(descriptor);
            for (Recipe recipe : matchingRecipes) {
                if (recMap.containsKey(recipe)) {
                    int currentTags = recMap.get(recipe);
                    recMap.put(recipe, currentTags + 1);
                } else {
                    recMap.put(recipe, 1);
                }
            }
        }

        return recMap;
    }
    
    // EFFECTS: given a collection of recipes, returns a dictionary with
    // keys as ingredients and values as number of times the ingredient is seen in recipes
    public HashMap<String, Integer> getIngredientsPerRecipe(List<Recipe> recipes) {
        HashMap<String, Integer> ingredientMap = new HashMap<>();
        for (Recipe recipe : recipes) {
            List<String> ingredients = recipe.getIngredients();
            for (String ingredient : ingredients) {
                if (ingredientMap.containsKey(ingredient)) {
                    int ingredientCount = ingredientMap.get(ingredient);
                    ingredientMap.put(ingredient, ingredientCount + 1);
                } else {
                    ingredientMap.put(ingredient, 1);
                }
            }
        }
        return ingredientMap;
    }

    // EFFECTS: given a map with recipe as key and number of matching tags as value, 
    // finds and returns all recipes with the highest number of matching tags
    public List<Recipe> getRecommendationList(HashMap<Recipe, Integer> recMap) {
        int maxTags = 0;
        List<Recipe> recList = new ArrayList<>();

        for (Recipe recipe : recMap.keySet()) {
            if (recMap.get(recipe) > maxTags) {
                maxTags = recMap.get(recipe);
            }

        }
        for (Recipe recipe : recMap.keySet()) {
            if (recMap.get(recipe) == maxTags) {
                recList.add(recipe);
            }
        }
        return recList;
    }

    // EFFECTS: given a map with ingredient as key and number of matching recipes as value,
    // finds and returns all ingredients with the highest number of matching recipes
    public List<String> getIngredientList(HashMap<String, Integer> ingredientMap) {
        int maxIngredients = 0;
        List<String> ingredientList = new ArrayList<>();

        for (String ingredient : ingredientMap.keySet()) {
            if (ingredientMap.get(ingredient) > maxIngredients) {
                maxIngredients = ingredientMap.get(ingredient);
            }
        }
        for (String ingredient : ingredientMap.keySet()) {
            if (ingredientMap.get(ingredient) == maxIngredients) {
                ingredientList.add(ingredient);
            }
        }

        return ingredientList;
    }

    public EventLog getLog() {
        return log;
    }
}


