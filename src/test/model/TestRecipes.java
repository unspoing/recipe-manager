package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestRecipes {
    private String name; 
    private List<String> tags;
    private List<String> ingredients;
    private List<String> steps;
    private Recipe recipe;
    private String name2; 
    private List<String> tags2;
    private List<String> ingredients2;
    private List<String> steps2;
    private Recipe recipe2;
    
    private List<Recipe> recipeList;
    private List<Recipe> recipeList2;
    private List<Recipe> recipeList3;
    private List<Recipe> recipeList4;
    
    private HashMap<Recipe, Integer> map1 = new HashMap<>();
    private HashMap<Recipe, Integer> map2 = new HashMap<>();
    private HashMap<Recipe, Integer> map3 = new HashMap<>();
    private HashMap<String, Integer> map4 = new HashMap<>();
    private HashMap<String, Integer> map5 = new HashMap<>();
    private HashMap<String, Integer> map6 = new HashMap<>();
    private Recipes recipes;

    @BeforeEach
    void runBefore() {
        tags = new ArrayList<>();
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
        recipeList = new ArrayList<>();
        recipeList2 = new ArrayList<>();
        recipeList3 = new ArrayList<>();
        recipeList4 = new ArrayList<>();
            
        name = "meow";
        tags.add("cat");
        tags.add("animal");
        ingredients.add("paws");
        ingredients.add("fur");
        steps.add("feed");
        steps.add("pet");
        recipe = new Recipe(name, tags, ingredients, steps);

        tags2 = new ArrayList<>();
        ingredients2 = new ArrayList<>();
        steps2 = new ArrayList<>();
            
        name2 = "bark";
        tags2.add("dog");
        tags2.add("animal");
        ingredients2.add("paws");
        ingredients2.add("tail");
        steps2.add("walk");
        steps2.add("feed");
        recipe2 = new Recipe(name2, tags2, ingredients2, steps2);

        recipeList.add(recipe);
        recipeList.add(recipe2);
        recipeList2.add(recipe);
        recipeList3.add(recipe2);
        
        map2.put(recipe, 2);
        map2.put(recipe2, 1);
        map3.put(recipe2, 2);
        map3.put(recipe, 1);

        map4.put("paws", 2);
        map4.put("fur", 1);
        map4.put("tail", 1);
        map5.put("paws",1);
        map5.put("fur", 1);
        map6.put("paws",1);
        map6.put("tail",1);



        recipes = new Recipes();

        
    }

    @Test
    void testConstructor() {
        assertTrue(recipes.getRecipes().isEmpty());
        assertEquals(recipes.getNumOfRecipes(), 0);
    }

    @Test
    void testGetRecipeWithIndex() {
        recipes.addRecipe(recipe);
        recipes.addRecipe(recipe2);
        assertEquals(recipes.getRecipeWithIndex(0), recipe);
        assertEquals(recipes.getRecipeWithIndex(1), recipe2);
    }

    @Test
    void testGetRecipeWithName() {
        recipes.addRecipe(recipe);
        recipes.addRecipe(recipe2);
        assertEquals(recipes.getRecipeWithname("bark"), recipe2);
        assertEquals(recipes.getRecipeWithname("meow"), recipe);
        assertEquals(recipes.getRecipeWithname("quack"), null);
    }

    @Test
    void testGetNumOfRecipes() {
        assertEquals(recipes.getNumOfRecipes(), 0);
        recipes.addRecipe(recipe);
        assertEquals(recipes.getNumOfRecipes(), 1);
        recipes.addRecipe(recipe2);
        assertEquals(recipes.getNumOfRecipes(), 2);
    }

    @Test
    void testAddRecipe() {
        recipes.addRecipe(recipe);
        assertEquals(recipes.getRecipes(), recipeList2);
        assertEquals(recipes.getNumOfRecipes(), 1);
        recipes.addRecipe(recipe2);
        assertEquals(recipes.getRecipes(), recipeList);
        assertEquals(recipes.getNumOfRecipes(), 2);

    }

    @Test
    void testgetAllWithTag() {
        recipes.addRecipe(recipe);
        recipes.addRecipe(recipe2);
        assertEquals(recipes.getAllWithTag("cat"), recipeList2);
        assertEquals(recipes.getAllWithTag("dog"), recipeList3);
        assertEquals(recipes.getAllWithTag("quack"), recipeList4);
        assertEquals(recipes.getAllWithTag("animal"), recipeList);
    }

    @Test
    void testgetAllWithIngredient() {
        recipes.addRecipe(recipe);
        recipes.addRecipe(recipe2);
        assertEquals(recipes.getAllWithIngredient("paws"), recipeList);
        assertEquals(recipes.getAllWithIngredient("fur"), recipeList2);
        assertEquals(recipes.getAllWithIngredient("tail"), recipeList3);
        assertEquals(recipes.getAllWithTag("quack"), recipeList4);
    }
    
    @Test
    void testGetTagsPerRecipe() {
        recipes.addRecipe(recipe);
        recipes.addRecipe(recipe2);
        HashMap<Recipe, Integer> testMap = recipes.getTagsPerRecipe(recipes, ingredients);
        assertEquals(testMap, map1);
        HashMap<Recipe, Integer> testMap2 = recipes.getTagsPerRecipe(recipes, tags);
        assertEquals(testMap2, map2);
        HashMap<Recipe, Integer> testMap3 = recipes.getTagsPerRecipe(recipes, tags2);
        assertEquals(testMap3, map3);

    }

    @Test
    void testGetIngredientsPerRecipe() {
        recipes.addRecipe(recipe);
        recipes.addRecipe(recipe2);
        HashMap<String, Integer> testMap = recipes.getIngredientsPerRecipe(recipeList);
        assertEquals(testMap, map4);
        HashMap<String, Integer> testMap2 = recipes.getIngredientsPerRecipe(recipeList2);
        assertEquals(testMap2, map5);
        HashMap<String, Integer> testMap3 = recipes.getIngredientsPerRecipe(recipeList3);
        assertEquals(testMap3, map6);
    }


    @Test
    void testGetRecommendedRecipe() {
        List<Recipe> recList = new ArrayList<>();
        List<Recipe> testList = new ArrayList<>();
        List<Recipe> testList2 = new ArrayList<>();
        List<String> tagList = new ArrayList<>();
        tagList.add("animal");
        testList.add(recipe);
        testList2.add(recipe2);
        testList2.add(recipe);
        recipes.addRecipe(recipe);
        recipes.addRecipe(recipe2);
        HashMap<Recipe, Integer> testMap = recipes.getTagsPerRecipe(recipes, tagList);
        recList = recipes.getRecommendationList(testMap);
        assertEquals(new HashSet<Recipe>(recList), new HashSet<Recipe>(testList2));
        tagList.add("cat");
        testMap = recipes.getTagsPerRecipe(recipes, tags);
        recList = recipes.getRecommendationList(testMap);
        assertEquals(recList, testList);
    }

    @Test
    void testGetIngredientRecommendation() {
        List<String> recList = new ArrayList<>();
        List<String> testList = new ArrayList<>();
        testList.add("paws");
        recipes.addRecipe(recipe);
        recipes.addRecipe(recipe2);
        HashMap<String, Integer> testMap = recipes.getIngredientsPerRecipe(recipeList);
        recList = recipes.getIngredientList(testMap);
        assertEquals(recList, testList);
    }

}
