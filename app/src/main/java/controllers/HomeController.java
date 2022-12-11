package controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import javafx.util.Duration;
import models.entities.Comment;
import models.entities.Ingredient;
import models.entities.Message;
import models.entities.Recipe;
import models.entities.Tag;
import models.entities.User;
import services.impl.IngredientServiceImpl;
import services.impl.RecipeServiceImpl;
import services.impl.TagServiceImpl;
import services.impl.UserServiceImpl;
import util.common.SceneContext;
import util.common.UserListener;
import util.constants.FailMessages;
import util.constants.SuccessMessages;
import util.exceptions.common.InvalidDateException;

public class HomeController implements Initializable {

    @FXML
    private Button addNewRecipe;

    @FXML
    private Button addToPlan;

    @FXML
    private Label userLbl;

    @FXML
    private Button favorites;

    @FXML
    private AnchorPane filterPane;

    @FXML
    private GridPane grid;

    @FXML
    private GridPane gridIngredient;

    @FXML
    private GridPane gridTag;

    @FXML
    private Button home;

    @FXML
    private Button openDetailed;

    @FXML
    private Button plan;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label cartCount;

    @FXML
    private Button logout;

    @FXML
    private Button msg;

    @FXML
    private Label msgCountLbl;

    @FXML
    private ImageView recipeImg;

    @FXML
    private Label recipeLbl;

    @FXML
    private ScrollPane scroll;

    @FXML
    private ScrollPane scrollIngredient;

    @FXML
    private ScrollPane scrollTag;

    @FXML
    private Button search;

    @FXML
    private Label filter;

    @FXML
    private Label filterBack;

    @FXML
    private AnchorPane anchorPaneBelowFilter;

    @FXML
    private TextField searchField;

    @FXML
    private Label tagsLbl;

    @FXML
    private Button searchIngredient;

    @FXML
    private Button searchTag;

    @FXML
    private TextField tagsSearchField;
    @FXML
    private TextField ingredientsSearchField;

    @FXML
    private Button removeFromPlan;

    @FXML
    private Button cart;

    @FXML
    private Button sendMsg;

    private UserServiceImpl userService = new UserServiceImpl();
    private RecipeServiceImpl recipeService = new RecipeServiceImpl();
    private IngredientServiceImpl ingredientService = new IngredientServiceImpl();
    private TagServiceImpl tagService = new TagServiceImpl();

    private User user = SceneContext.user;
    private Recipe recipe;
    private Message message;
    private Image image;
    private UserListener userListener;
    private LocalDate date;

    // Lists ----------------------------
    private List<Recipe> recipeList = recipeService.getAllRecipes();
    private List<Message> msgList = userService.getUserMessagesById(user.getId());
    private Map<Ingredient, Float> cartList = userService.getUserCartById(user.getId());
    private List<Recipe> favouriteRecipeList = userService.getFavoriteRecipes(user.getId());
    private Map<Recipe, Date> planList = userService.getWeeklyPlan(user.getId());

    private List<Ingredient> ingredientsList = ingredientService.getAllIngredients();
    private List<Ingredient> selectedIngredients = new ArrayList<>();
    private List<Tag> tagList = tagService.getAllTags(user.getId());
    private List<Tag> selectedTags = new ArrayList<>();

    private void chosenRecipe(Recipe recipe) {
        if (recipe == null) {
            anchorPaneBelowFilter.setVisible(false);
        } else {
            anchorPaneBelowFilter.setVisible(true);
            this.recipe = recipe;
            image = recipe.getPicture();
            recipeImg.setVisible(true);
            recipeImg.setImage(image);
            recipeLbl.setVisible(true);
            recipeLbl.setText(recipe.getName());
            if (user.getWeeklyList().keySet().contains(recipe.getId())) {
                removeFromPlan.setVisible(true);
                addToPlan.setVisible(false);
            } else {
                removeFromPlan.setVisible(false);
                addToPlan.setVisible(true);
            }
        }
    }

    private void chosenMsg(Message message) {
        this.message = message;

    }

    private void initializeHomeGrid() {
        grid.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            if (!recipeList.contains(recipe)) {
                chosenRecipe(null);
            }
            for (int i = 0; i < recipeList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/recipeItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                RecipeController recipeController = fxmlLoader.getController();
                if (user.getFavorites().contains(recipeList.get(i).getId())) {
                    String imgFile = "/img/heartFilled.png";
                    Image filled = new Image(getClass().getResourceAsStream(imgFile));
                    recipeController.setData(recipeList.get(i), filled, userListener);
                } else {
                    String imgFile = "/img/heartEmpty.png";
                    Image empty = new Image(getClass().getResourceAsStream(imgFile));
                    recipeController.setData(recipeList.get(i), empty, userListener);
                }
                if (column == 3) {
                    column = 0;
                    row++;
                }
                grid.setStyle("-fx-background-color: #ffffff");
                grid.add(anchorPane, column++, row);
                // Set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                // Set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeFavouritesGrid() {
        grid.getChildren().clear();

        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < favouriteRecipeList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/recipeItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                RecipeController recipeController = fxmlLoader.getController();
                String imgFile = "/img/heartFilled.png";
                Image filled = new Image(getClass().getResourceAsStream(imgFile));

                recipeController.setData(favouriteRecipeList.get(i), filled, userListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.setStyle("-fx-background-color: #ffffff");
                grid.add(anchorPane, column++, row);
                // Set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                // Set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializePlanGrid() {
        grid.getChildren().clear();

        LocalDate todayLocalDate = LocalDate.now();
        Date today = java.sql.Date.valueOf(todayLocalDate);

        int column = 0;
        int row = 1;
        try {
            List<Entry<Recipe, Date>> recipes = new ArrayList<>(planList.entrySet());
            recipes.sort(Entry.comparingByValue());

            for (Entry<Recipe, Date> recipe : recipes) {
                if (recipe.getValue().compareTo(today) < 0) {
                    userService.removeFromPlan(user.getId(), recipe.getKey().getId());
                    user.removeRecipeFromPlan(recipe.getKey().getId());
                    planList.remove(recipe.getKey());
                    continue;
                }
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/recipeItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                RecipeController recipeController = fxmlLoader.getController();

                if (user.getFavorites().contains(recipe.getKey().getId())) {
                    String imgFile = "/img/heartFilled.png";
                    Image filled = new Image(getClass().getResourceAsStream(imgFile));
                    recipeController.setDate(recipe.getValue());
                    recipeController.setData(recipe.getKey(), filled, userListener);
                } else {
                    String imgFile = "/img/heartEmpty.png";
                    Image empty = new Image(getClass().getResourceAsStream(imgFile));
                    recipeController.setDate(recipe.getValue());
                    recipeController.setData(recipe.getKey(), empty, userListener);
                }
                if (column == 3) {
                    column = 0;
                    row++;
                }
                grid.setStyle("-fx-background-color: #ffffff");
                grid.add(anchorPane, column++, row);
                // Set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                // Set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeMsgGrid() {
        grid.getChildren().clear();

        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < msgList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/messageItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                MsgController msgController = fxmlLoader.getController();
                msgController.setData(msgList.get(i), userListener);

                if (column == 1) {
                    column = 0;
                    row++;
                }

                grid.setStyle("-fx-background-color: #ffffff");
                grid.add(anchorPane, column++, row);
                // Set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                // Set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(5));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeAddNewRecipeGrid(Recipe recipe) {
        grid.getChildren().clear();
        recipeImg.setVisible(false);
        recipeLbl.setVisible(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/addNewRecipe.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            NewRecipeController newRecipeController = fxmlLoader.getController();
            if (recipe != null) {
                newRecipeController.setData(recipe, userListener);
            }
            grid.add(anchorPane, 1, 1);
            grid.setAlignment(Pos.CENTER);
            grid.setStyle("-fx-background-color: #ffa9a9");

            // Set grid width
            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);

            // Set grid height
            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTagGrid() {
        int column = 0;
        int row = 1;
        gridTag.getChildren().clear();
        try {
            for (int i = 0; i < tagList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/tag.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                TagController tagController = fxmlLoader.getController();

                List<UUID> selectedTagsIds = new ArrayList<>();
                for (Tag tag : selectedTags) {
                    selectedTagsIds.add(tag.getId());
                }

                if (selectedTagsIds.contains(tagList.get(i).getId())) {
                    tagController.checkBox();
                }

                tagController.setData(tagList.get(i), "HomeController", userListener);

                if (column == 1) {
                    column = 0;
                    row++;
                }
                gridTag.add(anchorPane, column++, row);
                // Set grid width
                gridTag.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridTag.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridTag.setMaxWidth(Region.USE_PREF_SIZE);

                // Set grid height
                gridTag.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridTag.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridTag.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(3));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeIngredientGrid() {
        int column = 0;
        int row = 1;
        gridIngredient.getChildren().clear();
        try {
            for (int i = 0; i < ingredientsList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/ingredient.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                IngredientController ingredientController = fxmlLoader.getController();

                // Adds the checkbox to selected ingredients
                List<UUID> selectedIngredientsIds = new ArrayList<>();
                for (Ingredient ingredient : selectedIngredients) {
                    selectedIngredientsIds.add(ingredient.getId());
                }

                if (selectedIngredientsIds.contains(ingredientsList.get(i).getId())) {
                    ingredientController.checkBox();
                }
                //

                ingredientController.setData(ingredientsList.get(i), userListener);

                if (column == 1) {
                    column = 0;
                    row++;
                }
                gridIngredient.add(anchorPane, column++, row);
                // Set grid width
                gridIngredient.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridIngredient.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridIngredient.setMaxWidth(Region.USE_PREF_SIZE);

                // Set grid height
                gridIngredient.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridIngredient.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridIngredient.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(3));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openDetailedGrid() {
        grid.getChildren().clear();
        recipeImg.setVisible(true);
        recipeLbl.setVisible(true);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/openForDetailed.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            DetailedViewController detailedViewController = fxmlLoader.getController();
            detailedViewController.setData(recipe, userListener);
            grid.setStyle("-fx-background-color: #ffa9a9");
            grid.add(anchorPane, 1, 1);
            // Set grid width
            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);
            // Set grid height
            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openReplyGrid(Message message) {
        grid.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/reply.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            ReplyController replyController = fxmlLoader.getController();
            replyController.setData(message, userListener);
            grid.setStyle("-fx-background-color: #ffa9a9");
            grid.add(anchorPane, 1, 1);
            // Set grid width
            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);
            // Set grid height
            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeCartGrid() {
        grid.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/cart.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            CartController cartController = fxmlLoader.getController();
            cartController.setData(userListener);
            grid.add(anchorPane, 1, 1);
            grid.setAlignment(Pos.CENTER);
            grid.setStyle("-fx-background-color: #ffa9a9");
            // Set grid width
            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);
            // Set grid height
            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeSendMsgGrid(Recipe recipe) {
        grid.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxmlFiles/sendMsg.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            SendMsgController sendMsgController = fxmlLoader.getController();
            sendMsgController.setData(recipe, userListener);
            grid.add(anchorPane, 1, 1);
            grid.setAlignment(Pos.CENTER);
            grid.setStyle("-fx-background-color: #ffa9a9");
            // Set grid width
            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);
            // Set grid height
            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ----------------------------------------- INITIALIZE
    // -----------------------------------------//

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // ComboBox User
        userLbl.setText(user.getNickname());

        // Set message count
        getMessages();

        // Set cart count
        getCart();

        // Slider -------------------------------------------------//
        filterPane.setVisible(false);
        anchorPaneBelowFilter.setTranslateY(-166);
        filter.setOnMousePressed(event -> {
            gridTag.getChildren().clear();
            gridIngredient.getChildren().clear();
            // ----------------------------------------------------//
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.7));
            slide.setNode(filterPane);
            slide.setToY(-176);
            slide.play();
            filterPane.setTranslateY(0);
            // ----------------------------------------------------//
            TranslateTransition slide2 = new TranslateTransition();
            slide2.setDuration(Duration.seconds(0.7));
            slide2.setNode(anchorPaneBelowFilter);
            slide2.setToY(-166);
            slide2.play();
            anchorPaneBelowFilter.setTranslateY(0);
            // ----------------------------------------------------//
            slide.setOnFinished((ActionEvent e) -> {
                filterPane.setVisible(false);
                filter.setVisible(false);
                filterBack.setVisible(true);
            });
        });

        filterBack.setOnMousePressed(event -> {
            scrollTag.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollIngredient.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            initializeTagGrid();
            initializeIngredientGrid();
            // ----------------------------------------------------//
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.7));
            slide.setNode(filterPane);
            slide.setToY(0);
            slide.play();
            filterPane.setVisible(true);
            filterPane.setTranslateY(-176);
            // ----------------------------------------------------//
            TranslateTransition slide2 = new TranslateTransition();
            slide2.setDuration(Duration.seconds(0.7));
            slide2.setNode(anchorPaneBelowFilter);
            slide2.setToY(0);
            slide2.play();
            anchorPaneBelowFilter.setTranslateY(-166);
            // ----------------------------------------------------//
            slide.setOnFinished((ActionEvent e) -> {
                filter.setVisible(true);
                filterBack.setVisible(false);

            });
        });

        // ------------------------------------------------------------------- MY
        // LISTENER
        chosenRecipe(null);
        userListener = new UserListener() {
            // When the user clicks on a specific recipe
            @Override
            public void onClickListener(Recipe recipe) {
                chosenRecipe(recipe);
            }

            @Override
            public void descriptionListener(Recipe recipe) {
                chosenRecipe(recipe);
            }

            // When the User clicks on the heart button
            @Override
            public void favClickListener(Recipe recipe, ImageView heartImage) {
                if (user.getFavorites().contains(recipe.getId())) {
                    userService.removeFromFavorites(user.getId(), recipe.getId());
                    favouriteRecipeList.remove(recipe);
                    String imgFile = "/img/heartEmpty.png";
                    Image empty = new Image(getClass().getResourceAsStream(imgFile));
                    heartImage.setImage(empty);
                } else {
                    userService.addToFavorites(user.getId(), recipe.getId());
                    favouriteRecipeList.add(recipe);
                    String imgFile = "/img/heartFilled.png";
                    Image filled = new Image(getClass().getResourceAsStream(imgFile));
                    heartImage.setImage(filled);
                }
            }

            // When the user clicks on a specific Ingredient
            @Override
            public void ingredientClickListener(Ingredient ingredient, ImageView ingredientButton) {
                if (selectedIngredients.contains(ingredient)) {
                    String imgFile = "/img/uncheck.png";
                    Image uncheck = new Image(getClass().getResourceAsStream(imgFile));
                    ingredientButton.setImage(uncheck);
                    selectedIngredients.remove(ingredient);
                } else {
                    String imgFile = "/img/check.png";
                    Image check = new Image(getClass().getResourceAsStream(imgFile));
                    ingredientButton.setImage(check);
                    selectedIngredients.add(ingredient);
                }
            }

            // When the user clicks on a specific Tag
            @Override
            public void tagClickListener(Tag tag) { //(Tag tag, ImageView tagButton)
                if (selectedTags.contains(tag)) {

                    selectedTags.remove(tag);
                } else {
                    selectedTags.add(tag);
                }
            }

            @Override
            public void recipeEntered(Recipe recipe, TextArea textArea) {
                textArea.setVisible(true);
                String[] descriptionWords = recipe.getDescription().split(" ");
                StringBuilder shortDescription = new StringBuilder();
                if (descriptionWords.length < 10) {
                    for (int i = 0; i < descriptionWords.length; i++) {
                        shortDescription.append(descriptionWords[i] + " ");
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        shortDescription.append(descriptionWords[i] + " ");
                    }
                }
                textArea.setText(shortDescription.toString());
                textArea.setOpacity(0.8);
                textArea.setWrapText(true);
            }

            @Override
            public void recipeExited(Recipe recipe, TextArea textArea) {
                textArea.setVisible(false);
            }

            @Override
            public void replyMsgListener(Message message) {
                chosenMsg(message);
                openReplyGrid(message);
            }

            @Override
            public void removeMsgListener(Message message) {
                userService.removeMessageById(message.getId());
                msgList.remove(message);
                String messageCount = String.valueOf(msgList.size());
                msgCountLbl.setText(messageCount);
                initializeMsgGrid();
            }

            // ------------- Close Listeners---------------//
            @Override
            public void closeMsgListener() {

                initializeMsgGrid();
            }

            @Override
            public void closeOpenForDetailed() {
                initializeHomeGrid();
            }

            @Override
            public void closeSendMsgListener() {
                initializeHomeGrid();
            }

            @Override
            public void closeCartListener() {
                initializeHomeGrid();
            }

            @Override
            public void removeRecipeListener() {
                recipeService.removeRecipe(recipe.getId());
                recipeList.remove(recipe);
                initializeHomeGrid();
            }

            @Override
            public void shareTheRecipeListener() {

                initializeSendMsgGrid(recipe);

            }

            @Override
            public void openRecipeListener(Recipe recipe) {
                chosenRecipe(recipe);
                openDetailedGrid();
            }

            @Override
            public void removeCommentListener(Comment comment) {
                userService.removeComment(comment.getId());
                openDetailedGrid();
            }

            @Override
            public void editCommentListener(Comment comment, String text) {
                try {
                    userService.editComment(comment.getId(), text);
                    showInformation(SuccessMessages.COMMENT_EDITED, null);
                } catch (Exception e) {
                    showError(FailMessages.COMMENT_EDIT_FAIL, e.getMessage());
                }
            }

            @Override
            public void addIngredientToCart(Ingredient ingredient, Float quantity) {
                userService.addToCart(user.getId(), ingredient.getId(), quantity);
                user.addIngredientToCart(ingredient.getId(), quantity);
                cartCount.setText(String.valueOf(user.getCart().size()));
                showInformation(String.format(SuccessMessages.CART_ADDED_INGREDIENT), null);
            }

            @Override
            public void removeIngredientFromCart(Ingredient ingredient) {
                userService.removeFromCart(user.getId(), ingredient.getId());
                user.removeIngredientFromCart(ingredient.getId());
                cartCount.setText(String.valueOf(user.getCart().size()));
                initializeCartGrid();
            }

            @Override
            public void editRecipeListener(Recipe recipe) {
                initializeAddNewRecipeGrid(recipe);
            }

            @Override
            public void addTagToRecipe(Tag tag) { // (Tag tag, ImageView tagButton)
                recipeService.addTagToRecipe(tag.getId(), recipe.getId());
            }

            @Override
            public void removeTagFromRecipe(Tag tag) {
                recipeService.removeTagFromRecipe(recipe.getId(), tag.getId());
            }

            @Override
            public void emptyCart() {
                cartCount.setText("0");
                
            }
        };

        // Initialize Home grid
        initializeHomeGrid();

        // -----------------------------------------BUTTONS-------------------------------------------

        // Search Button-----------------------------------------
        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                plan.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                home.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                grid.getChildren().clear();
                recipeList = recipeService.getRecipesWithNameLike(searchField.getText());
                List<Recipe> filteredRecipes = new ArrayList<>();
                for (Recipe recipe : recipeList) {
                    boolean isValid = true;

                    // Search tags
                    List<Tag> tags = tagService.getTagsByRecipeId(user.getId(), recipe.getId());
                    Set<String> tagNames = new HashSet<>();
                    for (Tag tag : tags) {
                        tagNames.add(tag.getName());
                    }
                    for (Tag tag : selectedTags) {
                        if (!tagNames.contains(tag.getName())) {
                            isValid = false;
                            break;
                        }
                    }

                    // Search ingredients
                    Map<Ingredient, Float> ingredients = ingredientService.getIngredientsByRecipeId(recipe.getId());
                    Set<String> ingredientNames = new HashSet<>();
                    for (Ingredient ingredient : ingredients.keySet()) {
                        ingredientNames.add(ingredient.getName());
                    }
                    for (Ingredient ingredient : selectedIngredients) {
                        if (!ingredientNames.contains(ingredient.getName())) {
                            isValid = false;
                            break;
                        }
                    }

                    if (isValid) {
                        filteredRecipes.add(recipe);
                    }
                }
                recipeList = filteredRecipes;
                selectedIngredients = new ArrayList<Ingredient>();
                selectedTags = new ArrayList<Tag>();
                initializeHomeGrid();
                initializeIngredientGrid();
                initializeTagGrid();
            }
        });

        searchIngredient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<Ingredient> filteredIngredient = ingredientService
                        .getIngredientWithNameLike(ingredientsSearchField.getText());
                ingredientsList = filteredIngredient;
                initializeIngredientGrid();
            }
        });
        searchTag.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridTag.getChildren().clear();
                List<Tag> filteredTags = tagService.getTagsWithNameLike(user.getId(), tagsSearchField.getText());
                System.out.println(filteredTags.size());
                tagList = filteredTags;
                initializeTagGrid();
            }
        });

        // Open for a detailed view Button-----------------------------------------
        openDetailed.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                plan.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                home.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                openDetailedGrid();
            }
        });

        // Add to a plan Button-----------------------------------------
        addToPlan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                plan.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                home.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                try {
                    LocalDate localDate = datePicker.getValue();
                    Date date = java.sql.Date.valueOf(localDate);
                    checkDateBeforeToday(date);
                    userService.addToPlan(user.getId(), recipe.getId(), date);
                    user.addRecipeToPlan(recipe.getId(), date);
                    planList.put(recipe, date);
                    removeFromPlan.setVisible(true);
                    addToPlan.setVisible(false);
                } catch (InvalidDateException e) {
                    showError(e.getMessage(), "");
                }
            }
        });

        removeFromPlan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                plan.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                home.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                userService.removeFromPlan(user.getId(), recipe.getId());
                user.removeRecipeFromPlan(recipe.getId());
                planList.remove(recipe);
                addToPlan.setVisible(true);
                removeFromPlan.setVisible(false);
                initializePlanGrid();
            }
        });

        // Add New Recipe Button-----------------------------------------
        addNewRecipe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                plan.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                home.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                initializeAddNewRecipeGrid(null);
            }
        });

        // Home Button-----------------------------------------

        home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                recipeImg.setVisible(true);
                recipeLbl.setVisible(true);
                home.setStyle("-fx-border-color: #000000;" + "-fx-color: rgb(239, 242, 255)");
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                plan.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");

                recipeList = recipeService.getAllRecipes();
                initializeHomeGrid();
            }
        });

        // Favourites Button-----------------------------------------
        favorites.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getMessages();
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-color: rgb(239, 242, 255)");
                home.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                plan.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                initializeFavouritesGrid();
            }
        });

        // Plan Button-----------------------------------------

        plan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getMessages();
                plan.setStyle("-fx-border-color: #000000;" + "-fx-color: rgb(239, 242, 255)");
                home.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                initializePlanGrid();
            }
        });

        // Cart --------------------------------------------------
        cart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                plan.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                home.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                initializeCartGrid();
            }
        });

        // Send Message --------------------------------------------------
        sendMsg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                plan.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                home.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                initializeSendMsgGrid(null);
            }
        });

        // Message Button-----------------------------------------
        msg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                plan.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                home.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                favorites.setStyle("-fx-border-color: #000000;" + "-fx-background-color: rgb(254, 215, 0)");
                initializeMsgGrid();
            }
        });

        // Logout Button-----------------------------------------

        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneContext.user = null;
                SceneContext.changeScene(event, "/fxmlFiles/login.fxml");
            }
        });
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    private void showInformation(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    private void checkDateBeforeToday(Date date) throws InvalidDateException {
        LocalDate todayLocalDate = LocalDate.now();
        Date today = java.sql.Date.valueOf(todayLocalDate);

        if (date.compareTo(today) < 0) {
            throw new InvalidDateException();
        }

    }

    private void getCart() {
        cartList = userService.getUserCartById(user.getId());
        cartCount.setText(String.valueOf(cartList.size()));
    }

    private void getMessages() {
        msgList = userService.getUserMessagesById(user.getId());
        msgCountLbl.setText(String.valueOf(msgList.size()));
    }
}
