package ui;

import model.Recipes;
import model.Recipe;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

// represents the recipe app
public class RecipeApp {
    private static final String JSON_STORE = "./data/recipes.json";
    private Scanner input;
    private Recipes recipeCollection;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    // EFFECTS: runs the recipe app
    public RecipeApp() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runRecipeApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runRecipeApp() {
        boolean running = true;
        String command = null;

        init();

        while (running) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                running = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }
    
    // EFFECTS: processes input
    private void processCommand(String command) {
        if (command.equals("v")) {
            viewRecipes();
        } else if (command.equals("r")) {
            viewRecommendations();
        } else if (command.equals("a")) {
            addRecipe();
        } else if (command.equals("s")) {
            saveData();
        } else if (command.equals("l")) {
            loadData();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes recipe collection
    private void init() {

        recipeCollection = new Recipes();
        input = new Scanner(System.in);
        input.useDelimiter("\r?\n|\r");
    }

    // EFFECTS: displays menu of options
    private void displayMenu() {
        System.out.println("\tv -> view recipes");
        System.out.println("\tr -> get recommendations");
        System.out.println("\ta -> add recipe");
        System.out.println("\tq -> quit");
        System.out.println("\ts -> save data");
        System.out.println("\tl -> load data");
    }

    // EFFECTS: displays recipe options
    private void displayRecipeOptions() {
        System.out.println("\nt -> view tagged recipes");
        System.out.println("\na -> view all recipes");
    }

    // EFFECTS: displays recommendation options
    private void displayRecommendOptions() {
        System.out.println("\nr -> view recommended recipes");
        System.out.println("\ni -> view recommended ingredients");
    }

    // MODIFIES: this
    // EFFECTS: displays then runs recipe viewing options
    private void viewRecipes() {
        String selection;
        displayRecipeOptions();
        selection = input.next();
        selection = selection.toLowerCase();
        if (selection.equals("t")) {
            viewTaggedRecipes();
        } else if (selection.equals("a")) {
            viewAllRecipes();
        } else {
            System.out.println("Selection not valid...");
        }

    }

    // EFFECTS: shows all recipes 
    private void viewAllRecipes() {
        for (Recipe recipe : recipeCollection.getRecipes()) {
            System.out.println(recipe.getName());
        }
    }

    // EFFECTS: shows all recipes with a specific tag
    private void viewTaggedRecipes() {
        List<Recipe> recipes;
        String tag;
        System.out.println("enter tag");
        tag = input.next();
        tag = tag.toLowerCase();

        recipes = recipeCollection.getAllWithTag(tag);

        for (Recipe recipe : recipes) {
            System.out.println(recipe.getName());
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a recipe to the recipe collection
    private void addRecipe() {
        String name;
        System.out.println("enter the recipe name");
        name = input.next();
        name = name.toLowerCase();

        String tags;
        System.out.println("enter the tags separated by spaces");
        tags = input.next();
        tags = tags.toLowerCase();
        List<String> tagList = Arrays.asList(tags.split(" "));

        String ingredients;
        System.out.println("enter the ingredients separated by spaces");
        ingredients = input.next();
        ingredients = ingredients.toLowerCase();
        List<String> ingredientsList = Arrays.asList(ingredients.split(" "));

        String steps;
        System.out.println("enter the steps separated by spaces");
        steps = input.next();
        steps = steps.toLowerCase();
        List<String> stepsList = Arrays.asList(steps.split(" "));

        Recipe newRecipe = new Recipe(name, tagList, ingredientsList, stepsList);
        recipeCollection.addRecipe(newRecipe);

    }

    // MODIFIES: this
    // EFFECTS: displays then runs recommendation options
    private void viewRecommendations() {
        String selection;
        displayRecommendOptions();;
        selection = input.next();
        selection = selection.toLowerCase();
        if (selection.equals("r")) {
            getRecommendedRecipe();
        } else if (selection.equals("i")) {
            getIngredientRecommendation();
        } else {
            System.out.println("Selection not valid...");
        }

    }

    // MODIFIES: this
    // EFFECTS: gets and prints recommended recipes
    private void getRecommendedRecipe() {
        String tags;
        System.out.println("enter the tags separated by spaces");

        tags = input.next();
        tags = tags.toLowerCase();

        List<String> tagsList = Arrays.asList(tags.split(" "));
        List<Recipe> recList = new ArrayList<>();
        HashMap<Recipe, Integer> recMap = recipeCollection.getTagsPerRecipe(recipeCollection, tagsList);

        recList = recipeCollection.getRecommendationList(recMap);

        for (Recipe recipe: recList) {
            System.out.println(recipe.getName());
        }
    }

    // MODIFIES: this
    // EFFECTS: gets and prints ingredient recommendations
    private void getIngredientRecommendation() {
        String tags;
        System.out.println("enter the tags separated by spaces");

        tags = input.next();
        tags = tags.toLowerCase();

        List<String> tagsList = Arrays.asList(tags.split(" "));
        List<Recipe> dishList = new ArrayList<>();
        HashMap<Recipe, Integer> recMap = recipeCollection.getTagsPerRecipe(recipeCollection, tagsList);

        dishList = recipeCollection.getRecommendationList(recMap);

        List<String> ingredientList = new ArrayList<>();
        HashMap<String, Integer> ingredientMap = recipeCollection.getIngredientsPerRecipe(dishList);

        ingredientList = recipeCollection.getIngredientList(ingredientMap);

        for (String ingredient: ingredientList) {
            System.out.println(ingredient);
        }
    }

    // EFFECTS: saves data to json file
    private void saveData() {
        try {
            jsonWriter.open();
            jsonWriter.write(recipeCollection);
            System.out.println("Saved Recipe Collection to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads data from json file
    private void loadData() {
        try {
            recipeCollection = jsonReader.read();
            System.out.println("Loaded Recipe Collection from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }


}
