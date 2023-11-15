package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.Map.Entry;
import models.entities.Ingredient;
import models.entities.Recipe;
import models.entities.User;
import services.IngredientService;
import services.impl.IngredientServiceImpl;
import services.impl.RecipeServiceImpl;
import util.common.NewRecipeListener;
import util.common.SceneContext;
import util.common.UserListener;
import util.constants.SuccessMessages;
public class NewRecipeController implements Initializable {

    @FXML
    private Button addImage;

    @FXML
    private Button addRecipe;

    @FXML
    private GridPane ingredientGrid;

    @FXML
    private ScrollPane ingredientScroll;

    @FXML
    private TextField ingredientSearch;

    @FXML
    private ImageView newRecipeImg;

    @FXML
    private TextArea recipeDescription;

    @FXML
    private GridPane recipeIngredientGrid;

    @FXML
    private ScrollPane recipeIngredients;

    @FXML
    private TextArea recipeInstruction;

    @FXML
    private TextField recipeName;

    @FXML
    private TextField recipeServeSize;

    @FXML
    private Button searchButton;

    @FXML
    private TextField txtFilename;

    @FXML
    private Button sizeEight;

    @FXML
    private Button sizeFour;

    @FXML
    private Button sizeSix;

    @FXML
    private Button sizeTwo;

    private RecipeServiceImpl recipeService = new RecipeServiceImpl();
    private IngredientService ingredientService = new IngredientServiceImpl();

    private UserListener userListener;
    private NewRecipeListener newRecipeListener;
    private User user = SceneContext.user;
    private File file;
    private Image image;
    private Recipe recipe;
    private Byte serveSize = 6;

    private List<Ingredient> allIngredients = new ArrayList<>();
    private List<Ingredient> filteredIngredients = new ArrayList<>();
    private Map<Ingredient, Float> selectedIngredients = new HashMap<>();

    private Stage createStage() {
        VBox vBox = new VBox(new Label("A Label"));
        Scene scene = new Scene(vBox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add Images");
        return stage;
    }

    private void initializeSelectedIngredientsGrid() {
        initializeGrid(selectedIngredients, recipeIngredientGrid, "SelectedNewRecipeController");
    }

    private <T> Map<T, Float> transformListToMap(List<T> ingredients) {
        Map<T, Float> map = new HashMap<>();
        for (T item : ingredients) {
            map.put(item, Float.valueOf("0"));
        }
        return map;
    }

    private void initializeAllIngredientsGrid() {
        allIngredients = ingredientService.getAllIngredients();
        for (Ingredient ingredient : selectedIngredients.keySet()) {
            allIngredients.remove(ingredient);
        }
        Map<Ingredient, Float> allIngredientsMap = transformListToMap(allIngredients);
        initializeGrid(allIngredientsMap, ingredientGrid, "NewRecipeController");
    }

    private void initializeFilteredIngredientsGrid(String name) {
        filteredIngredients = ingredientService.getIngredientWithNameLike(name);
        Map<Ingredient, Float> filteredIngredientsMap = transformListToMap(filteredIngredients);
        initializeGrid(filteredIngredientsMap, ingredientGrid, "NewRecipeController");
    }

    private void initializeGrid(Map<Ingredient, Float> ingredients, GridPane grid, String caller) {
        grid.getChildren().clear();
        List<Entry<Ingredient, Float>> ingredientsFloats = new ArrayList<>(ingredients.entrySet());
        int column = 0;
        int row = 1;
        try {
            for (Entry<Ingredient, Float> ingredientEntry : ingredientsFloats) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/ingredientItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                IngredientItemController ingredientItemController = fxmlLoader.getController();

                ingredientItemController.setData(ingredientEntry.getKey(), ingredientEntry.getValue(), caller,
                        userListener);
                ingredientItemController.setData(newRecipeListener);

                if (column == 1) {
                    column = 0;
                    row++;
                }
                grid.add(anchorPane, column++, row);
                // Set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                // Set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(3));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setData(Recipe recipe, UserListener userListener) {
        this.recipe = recipe;
        this.userListener = userListener;
        addRecipe.setText("Save recipe");
        recipeName.setText(recipe.getName());
        recipeDescription.setText(recipe.getDescription());
        recipeInstruction.setText(recipe.getInstructions());
        newRecipeImg.setImage(recipe.getPicture());
        serveSize = recipe.getServingSize();
        selectedIngredients = ingredientService.getIngredientsByRecipeId(recipe.getId());
        initializeGrid(selectedIngredients, recipeIngredientGrid, "SelectedNewRecipeController");

        if (serveSize == 2) {
            sizeTwo.setStyle("-fx-background-color: rgb(255, 255, 255)");
            sizeFour.setStyle("-fx-background-color: rgb(254, 215, 0)");
            sizeSix.setStyle("-fx-background-color: rgb(254, 215, 0)");
            sizeEight.setStyle("-fx-background-color: rgb(254, 215, 0)");
        } else if (serveSize == 4) {
            sizeTwo.setStyle("-fx-background-color: rgb(254, 215, 0)");
            sizeFour.setStyle("-fx-background-color: rgb(255, 255, 255)");
            sizeSix.setStyle("-fx-background-color: rgb(254, 215, 0)");
            sizeEight.setStyle("-fx-background-color: rgb(254, 215, 0)");
        } else if (serveSize == 6) {
            sizeTwo.setStyle("-fx-background-color: rgb(254, 215, 0)");
            sizeFour.setStyle("-fx-background-color: rgb(254, 215, 0)");
            sizeSix.setStyle("-fx-background-color: rgb(255, 255, 255)");
            sizeEight.setStyle("-fx-background-color: rgb(254, 215, 0)");
        } else if (serveSize == 8) {
            sizeTwo.setStyle("-fx-background-color: rgb(254, 215, 0)");
            sizeFour.setStyle("-fx-background-color: rgb(254, 215, 0)");
            sizeSix.setStyle("-fx-background-color: rgb(254, 215, 0)");
            sizeEight.setStyle("-fx-background-color: rgb(255, 255, 255)");
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newRecipeListener = new NewRecipeListener() {

            @Override
            public void addIngredientToRecipe(Ingredient ingredient, Float quantity) {
                selectedIngredients.put(ingredient, quantity);
                initializeSelectedIngredientsGrid();
                initializeAllIngredientsGrid();
            }

            @Override
            public void removeIngredientFromRecipe(Ingredient ingredient) {
                selectedIngredients.remove(ingredient);
                initializeSelectedIngredientsGrid();
                initializeAllIngredientsGrid();
            }

        };

        ingredientScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ingredientScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        recipeIngredients.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        recipeIngredients.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        initializeAllIngredientsGrid();

        addImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                // Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                fileChooser.getExtensionFilters().add(extFilter);
                file = fileChooser.showOpenDialog(createStage());
                if (file != null) {
                    txtFilename.setText(file.getAbsoluteFile().toString());
                    image = new Image(file.toURI().toString(), 100, 150, true, true);// path, prefWidth, prefHeight,
                                                                                     // preserveRatio, smooth
                    newRecipeImg.setImage(image);
                    newRecipeImg.setPreserveRatio(true);

                }
            }
        });

        addRecipe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (recipe != null) {
                    try {
                        recipeService.editRecipeName(recipe.getId(), recipeName.getText());
                        recipeService.editRecipeDescription(recipe.getId(), recipeDescription.getText());
                        recipeService.editRecipeInstructions(recipe.getId(), recipeInstruction.getText());
                        recipeService.editRecipeServingSize(recipe.getId(), serveSize);
                        recipeService.editRecipeImage(recipe.getId(), txtFilename.getText());
                        recipeService.removeRecipeIngredientsByRecipeId(recipe.getId());
                        recipeService.addRecipeIngredients(recipe.getId(), selectedIngredients);
                        showInformation(SuccessMessages.RECIPE_EDITED, null);
                        userListener.openRecipeListener(recipeService.getRecipeById(recipe.getId()));
                    } catch (Exception e) {
                        showError(e.getMessage());
                    }
                } else {
                    UUID recipeId = UUID.randomUUID();
                    String name = recipeName.getText();
                    System.out.println(name);
                    String picturePath = txtFilename.getText();
                    String description = recipeDescription.getText();
                    String instructions = recipeInstruction.getText();
                    UUID authorId = user.getId();
                    Map<Ingredient, Float> ingredients = new HashMap<>();
                    try {
                        recipeService.addRecipe(recipeId, name, picturePath, description, instructions, authorId,
                                ingredients, serveSize);
                        recipeService.addRecipeIngredients(recipeId, selectedIngredients);
                        SceneContext.changeScene(event, "/fxmlFiles/home.fxml");
                    } catch (Exception e) {
                        showError(e.getMessage());
                    }
                }
            }
        });

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initializeFilteredIngredientsGrid(ingredientSearch.getText());
            }
        });

        sizeTwo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                serveSize = 2;
                sizeTwo.setStyle("-fx-background-color: rgb(255, 255, 255)");
                sizeFour.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeSix.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeEight.setStyle("-fx-background-color: rgb(254, 215, 0)");
            }
        });

        sizeFour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                serveSize = 4;
                sizeTwo.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeFour.setStyle("-fx-background-color: rgb(255, 255, 255)");
                sizeSix.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeEight.setStyle("-fx-background-color: rgb(254, 215, 0)");

            }
        });

        sizeSix.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                serveSize = 6;
                sizeTwo.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeFour.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeSix.setStyle("-fx-background-color: rgb(255, 255, 255)");
                sizeEight.setStyle("-fx-background-color: rgb(254, 215, 2505)");
            }
        });

        sizeEight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                serveSize = 8;
                sizeTwo.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeFour.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeSix.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeEight.setStyle("-fx-background-color: rgb(255, 255, 255)");
            }
        });

    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void showInformation(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();

    }
}