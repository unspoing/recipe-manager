package ui;

import javax.swing.*;

import model.Recipe;
import model.Recipes;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RecipeAppGui extends JFrame implements ActionListener, WindowListener {
    private JLabel label;
    private ImageIcon image;
    private static final String JSON_STORE = "./data/recipes.json";
    private Recipes recipeCollection;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private JPanel cards;
    private JPanel menuCard;
    private JPanel recipeCard;
    private JPanel persistenceCard;
    private JPanel addingCard;
    private JPanel recommendCard;
    private CardLayout cardLayout;
    private JList<String> recipeList;
    private JList<String> recipeShown;
    private DefaultListModel<String> recipeModel;
    private String[] recipeShownArray;

    public RecipeAppGui() {
        JFrame frame = new JFrame("Recipe App");
        frame.addWindowListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(900, 400));
        

        init(frame.getContentPane());
        cardLayout = (CardLayout) (cards.getLayout());

        frame.pack();
        frame.setVisible(true);

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    public void windowClosing(WindowEvent e){
        for (Event log : recipeCollection.getLog()) {
            System.out.println(log.toString());
        }
    }

    public void windowOpened(WindowEvent e){}
    public void windowClosed(WindowEvent e){}
    public void windowIconified(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    public void windowActivated(WindowEvent e){}
    public void windowDeactivated(WindowEvent e){
    }

    
    //This is the method that is called when the the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("persistence")) {
            cardLayout.show(cards, "PERSISTENCEPANEL");
        } else if (e.getActionCommand().equals("view")) {
            recipeCard = getRecipeCard();
            cards.add(recipeCard, "RECIPEPANEL");
            cardLayout.show(cards, "RECIPEPANEL");
        } else if (e.getActionCommand().equals("back")) {
            cardLayout.show(cards, "MENUPANEL");
        } else if (e.getActionCommand().equals("save")) {
            saveData();
        } else if (e.getActionCommand().equals("load")) {
            loadData();
        } else if (e.getActionCommand().equals("select")) {
            showRecipe(recipeCard, recipeList.getSelectedValue());
        } else if (e.getActionCommand().equals("add")) {
            cardLayout.show(cards, "ADDINGPANEL");
        } else if (e.getActionCommand().equals("clear")) {
            clearFields(addingCard);
        } else if (e.getActionCommand().equals("addrecipe")) {
            addNewRecipe(addingCard);
        } else if (e.getActionCommand().equals("suggest")) {
            cardLayout.show(cards, "RECOMMENDPANEL");
        } else if (e.getActionCommand().equals("tagsRec")) {
            getRecommendedRecipes(recommendCard);
        } else if (e.getActionCommand().equals("ingredientsRec")) {
            getIngredientRecommendations(recommendCard);
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
            String[] recipes = new String[recipeCollection.getNumOfRecipes()];
            ArrayList<String> recipeNames = new ArrayList<>();

            for (Recipe recipe : recipeCollection.getRecipes()) {
                recipeNames.add(recipe.getName());
            }

            recipeNames.toArray(recipes);
            recipeList = new JList<String>(recipes);  
            System.out.println("Loaded Recipe Collection from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    private JPanel getMenuCard() {  
        JPanel menuCard = new JPanel();
        image = new ImageIcon(getClass().getResource("mylogo.png"));
        JLabel appLogo = new JLabel(image);

        JButton persistenceBut = getButton("save/load data", "persistence");

        JButton viewBut = getButton("view recipes", "view");

        JButton addBut = getButton("add recipes", "add");

        JButton suggestBut = getButton("get suggestions", "suggest");

        label = new JLabel("Recipe App");

        menuCard.add(label);
        menuCard.add(persistenceBut);
        menuCard.add(viewBut);
        menuCard.add(addBut);
        menuCard.add(suggestBut);
        menuCard.add(appLogo);

        return menuCard;
    }

    private JButton getButton(String name, String command) {
        JButton button = new JButton(name);
        button.setActionCommand(command);
        button.addActionListener(this);

        return button;
    }

    private JPanel getRecipeCard() {  
        JPanel recipeCard = new JPanel();
        image = new ImageIcon(getClass().getResource("myviewing.png"));
        JLabel appLogo = new JLabel(image);

        JButton backBut = getButton("go back", "back");
        JButton selectBut = getButton("view this recipe", "select");

        JScrollPane recipesPane = new JScrollPane(recipeList);
        recipesPane.setPreferredSize(new Dimension(300,100));

        JScrollPane selectedRecipe = new JScrollPane(recipeShown);
        selectedRecipe.setPreferredSize(new Dimension(300,100));

        recipeCard.add(backBut);
        recipeCard.add(recipesPane);
        recipeCard.add(selectBut);
        recipeCard.add(selectedRecipe);
        recipeCard.add(appLogo);

        return recipeCard;
    }

    private JPanel getPersistenceCard() {  
        JPanel persistenceCard = new JPanel();
        image = new ImageIcon(getClass().getResource("mypersistence.png"));
        JLabel appLogo = new JLabel(image);

        JButton saveBut = getButton("save data", "save");

        JButton loadBut = getButton("load data", "load");

        JButton backBut = getButton("go back", "back");

        persistenceCard.add(saveBut);
        persistenceCard.add(loadBut);
        persistenceCard.add(backBut);
        persistenceCard.add(appLogo);

        return persistenceCard;
    }

    private void init(Container pane) {
        cards = new JPanel(new CardLayout());

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        recipeCollection = new Recipes();

        menuCard = getMenuCard();
        recipeCard = getRecipeCard();
        persistenceCard = getPersistenceCard();
        addingCard = getAddingCard();
        recommendCard = getRecommendCard();

        cards.add(menuCard, "MENUPANEL");
        cards.add(recipeCard, "RECIPEPANEL");
        cards.add(persistenceCard, "PERSISTENCEPANEL");
        cards.add(addingCard, "ADDINGPANEL");
        cards.add(recommendCard, "RECOMMENDPANEL");

        pane.add(cards, BorderLayout.CENTER);

        String[] recipes = new String[recipeCollection.getNumOfRecipes()];
        ArrayList<String> recipeNames = new ArrayList<>();

        for (Recipe recipe : recipeCollection.getRecipes()) {
            recipeNames.add(recipe.getName());
        }

        recipeNames.toArray(recipes);
        recipeList = new JList<String>(recipes);
        recipeShown = new JList<String>(recipeShownArray);
        recipeModel = new DefaultListModel<>();
        recipeShown.setModel(recipeModel);
        
    }

    private void showRecipe(Container pane, String recipe) {
        Recipe selectedRecipe = null;
        for (Recipe currRecipe : recipeCollection.getRecipes()) {
            if (currRecipe.getName().equals(recipe)) {
                selectedRecipe = currRecipe;
            }
        }
        showSelectedRecipe(selectedRecipe);
    }

    private void showSelectedRecipe(Recipe recipe) {
        recipeModel.clear();

        String name = recipe.getName();
        String tags = "Tags: " + String.join(", ", recipe.getTags());
        String ingredients = "Ingredients: " + String.join(", ", recipe.getIngredients());

        recipeModel.addElement(name);
        recipeModel.addElement(tags);
        recipeModel.addElement(ingredients);
        recipeModel.addElement("Steps: ");

        int stepNum = 1;
        for (String step : recipe.getSteps()) {
            String stepString = Integer.toString(stepNum) + ". " + step;
            recipeModel.addElement(stepString);
            stepNum += 1;
        }
    }

    private JPanel getAddingCard() {
        JPanel addingCard = new JPanel();
        image = new ImageIcon(getClass().getResource("myadding.png"));
        JLabel appLogo = new JLabel(image);

        JTextField nameBox = new JTextField("",10);
        JScrollPane ingredientsBox = new JScrollPane(new JTextArea("",4,10));
        JScrollPane stepsBox = new JScrollPane(new JTextArea("",4,10));
        JScrollPane tagsBox = new JScrollPane(new JTextArea("",4,10));

        JButton addBut = getButton("add recipe", "addrecipe");

        JButton backBut = getButton("go back", "back");

        JButton clearBut = getButton("clear fields", "clear");

        addingCard.add(addBut);
        addingCard.add(clearBut);
        addingCard.add(backBut);
        addingCard.add(nameBox);
        addingCard.add(tagsBox);
        addingCard.add(ingredientsBox);
        addingCard.add(stepsBox);
        addingCard.add(appLogo);

        return addingCard;
    }

    private void clearFields(JPanel panel) {
        for (Component component: panel.getComponents()) {
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                JViewport viewport = (JViewport) scrollPane.getComponent(0);
                JTextArea textArea = (JTextArea) viewport.getView();
                textArea.setText("");
            }
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.setText("");
            }
        }
    }

    private Recipe getNewRecipe(JPanel panel) {
        String name = "";
        String tags = "";
        String ingredients = "";
        String steps = "";
        ArrayList<String> ingredientList = new ArrayList<>();
        ArrayList<String> tagList = new ArrayList<>();
        ArrayList<String> stepList = new ArrayList<>();
        int textAreaNum = 0;
        for (Component component: panel.getComponents()) {
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                JViewport viewport = (JViewport) scrollPane.getComponent(0);
                JTextArea textArea = (JTextArea) viewport.getView();
                if (textAreaNum == 0) {
                    tags = textArea.getText();
                    if (tags.isBlank()) {
                        return null;
                    }
                    tagList = new ArrayList<String>(Arrays.asList(tags.split("\r?\n|\r")));
                    textAreaNum += 1;
                } else if (textAreaNum == 1) {
                    ingredients = textArea.getText();
                    if (ingredients.isBlank()) {
                        return null;
                    }
                    ingredientList = new ArrayList<String>(Arrays.asList(ingredients.split("\r?\n|\r")));
                    textAreaNum += 1;
                } else if (textAreaNum == 2) {
                    steps = textArea.getText();
                    if (steps.isBlank()) {
                        return null;
                    }
                    stepList = new ArrayList<String>(Arrays.asList(steps.split("\r?\n|\r")));

                }
            }
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                name = textField.getText().strip();
                if (name.isBlank()) {
                    return null;
                }
            }
        }
        return (new Recipe(name, tagList, ingredientList, stepList));
    }

    public void addNewRecipe(JPanel panel) {
        Recipe newRecipe = getNewRecipe(panel);
        if ((newRecipe != null) && (null == recipeCollection.getRecipeWithname(newRecipe.getName()))) {
            recipeCollection.addRecipe(newRecipe);
            ArrayList<String> recipeNames = new ArrayList<>();
            String[] recipes = new String[recipeCollection.getNumOfRecipes()];
            for (Recipe recipe : recipeCollection.getRecipes()) {
                recipeNames.add(recipe.getName());
            }

            recipeNames.toArray(recipes);
            recipeList = new JList<String>(recipes);  
        }
    }

    private JPanel getRecommendCard() {  
        JPanel recommendCard = new JPanel();
        image = new ImageIcon(getClass().getResource("myrecommendations.png"));
        JLabel appLogo = new JLabel(image);

        JButton backBut = getButton("go back", "back");
        JButton tagsBut = getButton("suggested dishes", "tagsRec");
        JButton ingredientsBut = getButton("suggested ingredients", "ingredientsRec");

        JScrollPane tagsBox = new JScrollPane(new JTextArea("",4,10));
        tagsBox.setPreferredSize(new Dimension(200,80));

        JTextArea resultsArea = new JTextArea("",4,10);
        resultsArea.setEditable(false);
        JScrollPane resultsBox = new JScrollPane(resultsArea);
        resultsBox.setPreferredSize(new Dimension(200,80));

        recommendCard.add(backBut);
        recommendCard.add(tagsBox);
        recommendCard.add(tagsBut);
        recommendCard.add(ingredientsBut);
        recommendCard.add(resultsBox);
        recommendCard.add(appLogo);

        return recommendCard;
    }

    private List<String> getTagsFromPanel(JPanel panel) {
        for (Component component: panel.getComponents()) {
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                JViewport viewport = (JViewport) scrollPane.getComponent(0);
                JTextArea textArea = (JTextArea) viewport.getView();
                String tags = textArea.getText();
                if (tags.isBlank()) {
                    return null;
                }
                ArrayList<String> tagList = new ArrayList<>();
                for (String tag : tags.split("\r?\n|\r")) {
                    String trimmed = tag.strip().toLowerCase();
                    if (!trimmed.isBlank()) {
                        tagList.add(trimmed);
                    }
                }
                return tagList;
            }
        }
        return null;
    }

    private void setResultsText(JPanel panel, String text) {
        int textAreaNum = 0;
        for (Component component: panel.getComponents()) {
            if (component instanceof JScrollPane) {
                if (textAreaNum == 1) {
                    JScrollPane scrollPane = (JScrollPane) component;
                    JViewport viewport = (JViewport) scrollPane.getComponent(0);
                    JTextArea textArea = (JTextArea) viewport.getView();
                    textArea.setText(text);
                    return;
                }
                textAreaNum += 1;
            }
        }
    }

    private void getRecommendedRecipes(JPanel panel) {
        List<String> tagsList = getTagsFromPanel(panel);
        if (tagsList == null || tagsList.isEmpty()) {
            return;
        }

        HashMap<Recipe, Integer> recMap = recipeCollection.getTagsPerRecipe(recipeCollection, tagsList);
        List<Recipe> recommendations = recipeCollection.getRecommendationList(recMap);

        StringBuilder results = new StringBuilder();
        for (Recipe recipe : recommendations) {
            results.append(recipe.getName()).append("\n");
        }
        setResultsText(panel, results.toString());
    }

    private void getIngredientRecommendations(JPanel panel) {
        List<String> tagsList = getTagsFromPanel(panel);
        if (tagsList == null || tagsList.isEmpty()) {
            return;
        }

        HashMap<Recipe, Integer> recMap = recipeCollection.getTagsPerRecipe(recipeCollection, tagsList);
        List<Recipe> dishList = recipeCollection.getRecommendationList(recMap);
        HashMap<String, Integer> ingredientMap = recipeCollection.getIngredientsPerRecipe(dishList);
        List<String> ingredientList = recipeCollection.getIngredientList(ingredientMap);

        StringBuilder results = new StringBuilder();
        for (String ingredient : ingredientList) {
            results.append(ingredient).append("\n");
        }
        setResultsText(panel, results.toString());
    }
}