package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import models.entities.Comment;
import models.entities.Ingredient;
import models.entities.Recipe;
import models.entities.Tag;
import models.entities.User;
import services.IngredientService;
import services.RecipeService;
import services.TagService;
import services.UserService;
import services.impl.IngredientServiceImpl;
import services.impl.RecipeServiceImpl;
import services.impl.TagServiceImpl;
import services.impl.UserServiceImpl;
import util.common.SceneContext;
import util.common.UserListener;
import util.constants.FailMessages;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

public class DetailedViewController implements Initializable {
    private UserListener userListener;

    @FXML
    private Button addComment;

    @FXML
    private TextArea addCommentArea;

    @FXML
    private GridPane commentsGrid;

    @FXML
    private ScrollPane commentsScroll;

    @FXML
    private AnchorPane detailedAnchor;

    @FXML
    private Button edit;

    @FXML
    private Button imageButton;

    @FXML
    private ScrollPane ingredientScroll;

    @FXML
    private GridPane ingredientsGrid;

    @FXML
    private TextArea recipeName;

    @FXML
    private TextArea recipeDescription;

    @FXML
    private ImageView recipeImg;

    @FXML
    private TextArea recipeInstruction;

    @FXML
    private Button remove;

    @FXML
    private GridPane tagsGrid;

    @FXML
    private ScrollPane tagsScroll;

    @FXML
    private TextArea servingSize;

    @FXML
    private TextField tagTxtField;

    @FXML
    private Button addTag;

    @FXML
    private Button sizeEight;

    @FXML
    private Button sizeFour;

    @FXML
    private Button sizeSix;

    @FXML
    private Button sizeTwo;

    @FXML
    void close(MouseEvent event) {
        userListener.closeOpenForDetailed();
    }

    @FXML
    void remove(MouseEvent event) {
        userListener.removeRecipeListener();
    }

    @FXML
    void shareTheRecipe(MouseEvent event) {
        userListener.shareTheRecipeListener();
    }

    private Recipe recipe;
    private User user = SceneContext.getUser();

    private IngredientService ingredientService = new IngredientServiceImpl();
    private TagService tagService = new TagServiceImpl();
    private RecipeService recipeService = new RecipeServiceImpl();
    private UserService userService = new UserServiceImpl();

    private Map<Ingredient, Float> ingredientsList = new HashMap<>();
    private List<Tag> tagsList = new ArrayList<>();
    private List<Comment> commentsList = new ArrayList<>();

    private List<UUID> addedTagsIds = new ArrayList<>();

    private void initializeIngredientGrid() {
        ingredientScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ingredientScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ingredientsGrid.getChildren().clear();
        Set<Ingredient> ingredients = ingredientsList.keySet();
        int column = 0;
        int row = 1;
        try {
            for (Ingredient ingredient : ingredients) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/ingredientItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                IngredientItemController ingredientItemController = fxmlLoader.getController();
                Float quantity = ingredientsList.get(ingredient);
                ingredientItemController.setData(ingredient, quantity, "DetailedViewController", userListener);

                if (column == 1) {
                    column = 0;
                    row++;
                }
                ingredientsGrid.add(anchorPane, column++, row);
                // Set grid width
                ingredientsGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                ingredientsGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                ingredientsGrid.setMaxWidth(Region.USE_PREF_SIZE);

                // Set grid height
                ingredientsGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                ingredientsGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                ingredientsGrid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(3));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initializeCommentsGrid() {
        commentsGrid.getChildren().clear();
        commentsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        commentsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < commentsList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/comment.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                CommentController commentController = fxmlLoader.getController();
                commentController.setData(commentsList.get(i), userListener);

                if (column == 1) {
                    column = 0;
                    row++;
                }
                commentsGrid.add(anchorPane, column++, row);
                // Set grid width
                commentsGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                commentsGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                commentsGrid.setMaxWidth(Region.USE_PREF_SIZE);

                // Set grid height
                commentsGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                commentsGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                commentsGrid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(3));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initializeTagsGrid() {
        tagsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tagsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        List<Tag> tags = tagService.getAllTags(user.getId());
        

        int column = 0;
        int row = 1;
        try {
            tagsGrid.getChildren().clear();
            for (Tag tag : tags) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/tag.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                TagController tagController = fxmlLoader.getController();
                Float quantity = ingredientsList.get(tag);                
                tagController.setData(tag, "DetailedViewController", userListener);
                if (addedTagsIds.contains(tag.getId())) {
                    tagController.setSelected(true);
                }
                if (column == 1) {
                    column = 0;
                    row++;
                }
                tagsGrid.add(anchorPane, column++, row);
                // Set grid width
                tagsGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                tagsGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                tagsGrid.setMaxWidth(Region.USE_PREF_SIZE);

                // Set grid height
                tagsGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                tagsGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                tagsGrid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(3));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }    
    }


    public void setData(Recipe recipe, UserListener userListener) {
        this.recipe = recipe;
        this.userListener = userListener;
        recipeName.setText(recipe.getName());
        recipeDescription.setText(recipe.getDescription());
        recipeImg.setImage(recipe.getPicture());
        recipeInstruction.setText(recipe.getInstructions());
        if (recipe.getServingSize() == 2) {
            sizeTwo.setStyle("-fx-background-color: rgb(255, 255, 255)");
        } else if (recipe.getServingSize() == 4) {
            sizeFour.setStyle("-fx-background-color: rgb(255, 255, 255)");
        }else if (recipe.getServingSize() == 6) {
            sizeSix.setStyle("-fx-background-color: rgb(255, 255, 255)");
        }else if (recipe.getServingSize() == 8) {
            sizeEight.setStyle("-fx-background-color: rgb(255, 255, 255)");
        }

        ingredientsList = ingredientService.getIngredientsByRecipeId(recipe.getId());;
        tagsList = userService.getUserRecipeTags(user.getId(), recipe.getId());
        addedTagsIds = tagService.getTagIdsByRecipeId(user.getId(), recipe.getId());
        commentsList = recipeService.getCommentsByRecipeId(recipe.getId());

        if (user.getId().equals(recipe.getAuthor())) {
            edit.setVisible(true);
            remove.setVisible(true);
        }
        initializeIngredientGrid();
        initializeCommentsGrid();
        initializeTagsGrid();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeIngredientGrid();
        initializeCommentsGrid();


        addComment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UUID commentId = UUID.randomUUID();
                String commentText = addCommentArea.getText();
                try {
                    userService.addComment(commentId, user.getId(), recipe.getId(), commentText);
                    commentsList.add(recipeService.getCommentById(commentId));
                    initializeCommentsGrid();
                } catch (Exception e) {
                    showAlert(FailMessages.COMMENT_ADD_FAIL, e.getMessage());
                }
            }
        });

        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                userListener.editRecipeListener(recipe);
            }
        });

        addTag.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tagService.getTagByName(user.getId(), tagTxtField.getText()) != null) {
                    System.out.println("Found it");
                    Tag tag = tagService.getTagByName(user.getId(), tagTxtField.getText());
                    if (!addedTagsIds.contains(tag.getId())) {
                        recipeService.addTagToRecipe(tag.getId(), recipe.getId());
                        tagsList.add(tag);
                        addedTagsIds.add(tag.getId());
                        initializeTagsGrid();
                    } else {
                        showAlert(FailMessages.TAG_ADD_FAIL, "Tag already added");
                    }
                } else {
                    System.out.println("Didn't find it");
                    UUID tagId = UUID.randomUUID();
                    tagService.addTag(tagId, tagTxtField.getText(), user.getId());
                    Tag tag = tagService.getTagById(tagId);
                    recipeService.addTagToRecipe(tag.getId(), recipe.getId());
                    tagsList.add(tag);
                    addedTagsIds.add(tag.getId());
                    initializeTagsGrid();

                }
            }
        });

        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                recipeService.removeRecipe(recipe.getId());
            }
        });
        
        sizeTwo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ingredientsList = ingredientService.getIngredientsByRecipeId(recipe.getId());
                sizeTwo.setStyle("-fx-background-color: rgb(255, 255, 255)");
                sizeFour.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeSix.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeEight.setStyle("-fx-background-color: rgb(254, 215, 0)");

                for (Ingredient ingredient : ingredientsList.keySet()) {
                    Float quantityForOne = ingredientsList.get(ingredient) / recipe.getServingSize();
                    ingredientsList.replace(ingredient, quantityForOne * 2);
                }
                initializeIngredientGrid();
            }
        });

        sizeFour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ingredientsList = ingredientService.getIngredientsByRecipeId(recipe.getId());
                sizeTwo.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeFour.setStyle("-fx-background-color: rgb(255, 255, 255)");
                sizeSix.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeEight.setStyle("-fx-background-color: rgb(254, 215, 0)");

                for (Ingredient ingredient : ingredientsList.keySet()) {
                    Float quantityForOne = ingredientsList.get(ingredient) / recipe.getServingSize();
                    ingredientsList.replace(ingredient, quantityForOne * 4);
                }
                initializeIngredientGrid();
            }
        });

        sizeSix.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ingredientsList = ingredientService.getIngredientsByRecipeId(recipe.getId());
                sizeTwo.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeFour.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeSix.setStyle("-fx-background-color: rgb(255, 255, 255)");
                sizeEight.setStyle("-fx-background-color: rgb(254, 215, 0)");

                for (Ingredient ingredient : ingredientsList.keySet()) {
                    Float quantityForOne = ingredientsList.get(ingredient) / recipe.getServingSize();
                    ingredientsList.replace(ingredient, quantityForOne * 6);
                }
                initializeIngredientGrid();
            }
        });
   
        sizeEight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ingredientsList = ingredientService.getIngredientsByRecipeId(recipe.getId());
                sizeTwo.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeFour.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeSix.setStyle("-fx-background-color: rgb(254, 215, 0)");
                sizeEight.setStyle("-fx-background-color: rgb(255, 255, 255)");

                for (Ingredient ingredient : ingredientsList.keySet()) {
                    Float quantityForOne = ingredientsList.get(ingredient) / recipe.getServingSize();
                    ingredientsList.replace(ingredient, quantityForOne * 8);
                }
                initializeIngredientGrid();
            }
        });
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

}
