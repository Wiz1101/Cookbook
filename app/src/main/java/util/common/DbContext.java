package util.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javafx.scene.image.Image;
import models.entities.Comment;
import models.entities.Ingredient;
import models.entities.Message;
import models.entities.Recipe;
import models.entities.Tag;
import models.entities.User;
import util.constants.FailMessages;
import util.constants.SqlQueries;
import util.constants.SuccessMessages;
import util.constants.Variables;

public class DbContext {
    private static Connection conn;

    public DbContext(int port, String user, String pass) {

        try {
            conn = DriverManager.getConnection(Variables.DATABASE_SERVER_URL,
                    Variables.DATABASE_USER, Variables.DATABASE_PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------------------- INIT -------------------//
    public void useDatabase() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(SqlQueries.setMaxAllowedPackage);
        preparedStatement.execute();
        preparedStatement = conn.prepareStatement(SqlQueries.useDatabase);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void createDatabase() {
        try {
            conn = DriverManager.getConnection(Variables.DATABASE_COOKBOOK_URL, Variables.DATABASE_USER,
                    Variables.DATABASE_PASS);
            useDatabase();
        } catch (SQLException e) {
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(SqlQueries.setMaxAllowedPackage);
                preparedStatement.execute();
                preparedStatement = conn.prepareStatement(SqlQueries.createDatabase);
                preparedStatement.execute();

                useDatabase();
                try {
                    createTables();
                    useDatabase();
                    importRecords();
                    System.out.println("CREATED DATABASE SUCCESSFULLY !!!");
                    System.out.println("USING DATABASE ....");
                } catch (SQLException ex) {
                    ex.printStackTrace(); // modified here
                    System.out.println("FAILED TO CREATE TABLES ...");
                }
            } catch (SQLException exe) {
                exe.printStackTrace(); // modified here
                System.out.println("FAILED TO CREATE DATABASE");
            }
        }
        try {
            useDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute(SqlQueries.createTableUserTags);
        stmt.execute(SqlQueries.createTableUsers);
        stmt.execute(SqlQueries.createTableMessages);
        stmt.execute(SqlQueries.createTableIngredients);
        stmt.execute(SqlQueries.createTableUserIngredients);
        stmt.execute(SqlQueries.createTableRecipes);
        stmt.execute(SqlQueries.createTableComments);
        stmt.execute(SqlQueries.createTableRecipeIngredients);
        stmt.execute(SqlQueries.createTableRecipeTags);
        stmt.execute(SqlQueries.createTableUsersFavorites);
        stmt.execute(SqlQueries.createTableWeeklyPlans);
        stmt.execute(SqlQueries.createTableUsersRecipeTags);
        stmt.close();
    }

    public void importRecords() {
        importUsers();
        importRecipes();
        importTags();
        importMessages();
        importComments();
        importIngredients();
        importRecipeIngredients();
    }

    private void importIngredients() {
        String ingredientUnitsFile = "/src/main/resources/data/ingredientUnits.csv";
        String ingredientUnitsPath = System.getProperty("user.dir") + ingredientUnitsFile;
        ArrayList<String[]> ingredientUnits = FileIo.readFromFileSaveToArrayList(ingredientUnitsPath);
        int counter = 0;
        for (String[] ingredient : ingredientUnits) {
            counter++;
            UUID ingredientId = UUID.fromString(ingredient[0]);
            String name = ingredient[1];
            String unit = ingredient[2];
            addIngredient(ingredientId, name, unit);
        }
        System.out.println("Imported " + counter + " ingredients.");
    }

    private void importComments() {
        String commentsFile = "/src/main/resources/data/comments.csv";
        String commentsPath = System.getProperty("user.dir") + commentsFile;
        ArrayList<String[]> comments = FileIo.readFromFileSaveToArrayList(commentsPath);
        int counter = 0;
        for (String[] comment : comments) {
            counter++;
            UUID commentId = UUID.fromString(comment[0]);
            UUID userId = UUID.fromString(comment[1]);
            UUID recipeId = UUID.fromString(comment[2]);
            String text = comment[3];
            addComment(commentId, userId, recipeId, text);
        }
        System.out.println("Imported " + counter + " comments.");
    }

    private void importMessages() {
        String messagesFile = "/src/main/resources/data/messages.csv";
        String messagesPath = System.getProperty("user.dir") + messagesFile;
        ArrayList<String[]> messages = FileIo.readFromFileSaveToArrayList(messagesPath);
        int counter = 0;
        for (String[] message : messages) {
            counter++;
            UUID messageId = UUID.fromString(message[0]);
            UUID senderId = UUID.fromString(message[1]);
            UUID receiverId = UUID.fromString(message[2]);
            String text = message[3];
            Boolean isRead = Boolean.parseBoolean(message[4]);
            UUID recipeId = null;
            if (!message[5].equals("NULL")) {
                recipeId = UUID.fromString(message[5]);
            }
            sendMessage(messageId, senderId, receiverId, text, isRead, recipeId);
        }
        System.out.println("Imported " + counter + " messages.");

    }

    private void importTags() {
        String tagsFile = "/src/main/resources/data/tags.csv";
        String tagsPath = System.getProperty("user.dir") + tagsFile;
        ArrayList<String[]> tags = FileIo.readFromFileSaveToArrayList(tagsPath);
        int counter = 0;
        for (String[] tag : tags) {
            UUID tagId = UUID.fromString(tag[0]);
            String name = tag[1];
            UUID userId = null;
            if (!tag[2].equals("NULL")) {
                userId = UUID.fromString(tag[2]);
            }

            addTag(tagId, name, userId);
            counter++;
        }
        System.out.println("Imported " + counter + " tags.");
    }

    private void importRecipes() {
        String recipesFile = "/src/main/resources/data/recipes.csv";
        String recipesPath = System.getProperty("user.dir") + recipesFile;
        ArrayList<String[]> recipes = FileIo.readFromFileSaveToArrayList(recipesPath);
        int counter = 0;
        for (String[] recipe : recipes) {
            counter++;
            UUID recipeId = UUID.fromString(recipe[0]);
            String name = recipe[1];
            String picturePath = recipe[2];
            String description = recipe[3];
            String instructions = recipe[4];
            Byte servingSize = Byte.parseByte(recipe[5]);
            UUID authorId = UUID.fromString(recipe[6]);
            addRecipe(recipeId, name, picturePath, description, instructions, servingSize, authorId);
        }
        System.out.println("Imported " + counter + " recipes.");
    }

    private void importUsers() {
        String usersFile = "/src/main/resources/data/users.csv";
        String usersPath = System.getProperty("user.dir") + usersFile;
        ArrayList<String[]> users = FileIo.readFromFileSaveToArrayList(usersPath);
        int counter = 0;
        for (String[] user : users) {
            counter++;
            UUID userId = UUID.fromString(user[0]);
            String username = user[1];
            String email = user[2];
            String hashedPassword = user[3];
            addUser(userId, username, email, hashedPassword);
        }
        System.out.println("Imported " + counter + " users.");
    }

    private void importRecipeIngredients() {
        String recipesIngredientsFile = "/src/main/resources/data/ingredients.csv";
        String recipesIngredientsPath = System.getProperty("user.dir") + recipesIngredientsFile;
        ArrayList<String[]> recipesIngredients = FileIo.readFromFileSaveToArrayList(recipesIngredientsPath);
        for (String[] recipeIngredients : recipesIngredients) {
            UUID recipeId = UUID.fromString(recipeIngredients[0]);
            UUID ingredientId = UUID.fromString(recipeIngredients[1]);
            Float quantity = Float.parseFloat(recipeIngredients[2]);

            addRecipeIngredient(recipeId, ingredientId, quantity);
        }
    }

    // ------------------- USER -------------------//

    public String addUser(UUID userId, String username, String email, String hashedPassword) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addUser);
            ps.setString(1, userId.toString());
            ps.setString(2, username);
            ps.setString(3, username);
            ps.setString(4, email);
            ps.setString(5, hashedPassword);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return String.format(FailMessages.USER_ADD_FAIL);
        }
        return String.format(SuccessMessages.USER_ADDED);
    }

    public String removeUserById(UUID userId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.removeUserWithId);
            ps.setString(1, userId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.USER_DELETED);
            } else {
                return String.format(FailMessages.USER_DELETE_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.USER_DELETE_FAIL);
        }
    }

    public List<User> getUsersLike(String text) {
        List<User> users = new ArrayList<>();
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getUsersLike);
            ps.setString(1, "%" + text + "%");
            ps.setString(2, "%" + text + "%");
            ps.setString(3, "%" + text + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = getUserById(UUID.fromString(rs.getString("id")));
                users.add(user);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserByCredentials(String username, String password) {
        User user = null;

        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getUserByCredentials);
            ps.setString(1, username);
            ps.setString(2, Hasher.hashString(password));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UUID userId = UUID.fromString(rs.getString("id"));
                user = getUserById(userId);
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserById(UUID id) {
        User user = null;
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getUserById);
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String nickname = rs.getString("display_name");
                String email = rs.getString("email");
                String password = rs.getString("password");

                Map<UUID, Float> cart = getCartIdsByUserId(id);
                List<UUID> messages = getMessageIdsByUserId(id);
                Map<UUID, Date> weeklyList = getWeeklyListIdsByUserId(id);
                List<UUID> favorites = getFavoriteIdsByUserId(id);

                user = new User(id, username, nickname, email, password, cart, messages, weeklyList, favorites);
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public User getUserByUsername(String username) {
        User user = null;
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getUserByUsername);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String email = rs.getString("email");
                String nickname = rs.getString("display_name");
                String password = rs.getString("password");

                Map<UUID, Float> cart = getCartIdsByUserId(id);
                List<UUID> messages = getMessageIdsByUserId(id);
                Map<UUID, Date> weeklyList = getWeeklyListIdsByUserId(id);
                List<UUID> favorites = getFavoriteIdsByUserId(id);

                user = new User(id, username, nickname, email, password, cart, messages, weeklyList, favorites);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByNickname(String nickname) {
        User user = null;
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getUserByNickname);
            ps.setString(1, nickname);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");

                Map<UUID, Float> cart = getCartIdsByUserId(id);
                List<UUID> messages = getMessageIdsByUserId(id);
                Map<UUID, Date> weeklyList = getWeeklyListIdsByUserId(id);
                List<UUID> favorites = getFavoriteIdsByUserId(id);

                user = new User(id, username, nickname, email, password, cart, messages, weeklyList, favorites);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public User getUserByEmail(String email) {
        User user = null;
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getUserByEmail);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String username = rs.getString("username");
                String nickname = rs.getString("display_name");
                String password = rs.getString("password");

                Map<UUID, Float> cart = getCartIdsByUserId(id);
                List<UUID> messages = getMessageIdsByUserId(id);
                Map<UUID, Date> weeklyList = getWeeklyListIdsByUserId(id);
                List<UUID> favorites = getUserFavoriteIdsByUserId(id);

                user = new User(id, username, nickname, email, password, cart, messages, weeklyList, favorites);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            useDatabase();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SqlQueries.getAllUsers);

            while (rs.next()) {
                UUID userId = UUID.fromString(rs.getString("id"));
                users.add(getUserById(userId));
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Recipe> getUserFavoritesById(UUID userId) {
        List<Recipe> recipes = new ArrayList<>();
        List<UUID> recipeIds = getUserFavoriteIdsByUserId(userId);
        for (UUID recipeId : recipeIds) {
            recipes.add(getRecipeById(recipeId));
        }
        return recipes;
    }

    private List<UUID> getUserFavoriteIdsByUserId(UUID userId) {
        List<UUID> favoriteIds = new ArrayList<>();

        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getUserFavorites);
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID recipeId = UUID.fromString(rs.getString("recipe_id"));

                favoriteIds.add(recipeId);
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favoriteIds;
    }

    public boolean addRecipeToFavorites(UUID userId, UUID recipeId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addRecipeFavorite);
            ps.setString(1, userId.toString());
            ps.setString(2, recipeId.toString());
            ps.execute();
            ps.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeRecipeFromFavorites(UUID userId, UUID recipeId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.removeRecipeFavorite);
            ps.setString(1, userId.toString());
            ps.setString(2, recipeId.toString());
            ps.execute();
            ps.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Map<Recipe, Date> getWeeklyListByUserId(UUID userId) {
        Map<Recipe, Date> recipes = new HashMap<>();
        Map<UUID, Date> recipeIds = getWeeklyListIdsByUserId(userId);
        for (UUID recipeId : recipeIds.keySet()) {
            Recipe recipe = getRecipeById(recipeId);
            Date date = recipeIds.get(recipeId);

            recipes.put(recipe, date);
        }
        return recipes;
    }

    public Map<UUID, Date> getWeeklyListIdsByUserId(UUID userId) {
        Map<UUID, Date> weeklyListIds = new Hashtable<>();

        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getUserWeeklyList);
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID recipeId = UUID.fromString(rs.getString("recipe_id"));
                Date date = rs.getDate("date");

                weeklyListIds.put(recipeId, date);
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeklyListIds;
    }

    public Message getMessageById(UUID messageId) {
        Message message = null;
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getMessageById);
            ps.setString(1, messageId.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID senderId = UUID.fromString(rs.getString("sender_id"));
                UUID receiverId = UUID.fromString(rs.getString("receiver_id"));
                String text = rs.getString("message_text");
                Boolean isRead = rs.getBoolean("is_read");
                UUID recipeId = null;
                if (rs.getString("recipe_id") != null) {
                    recipeId = UUID.fromString(rs.getString("recipe_id"));
                }
                message = new Message(messageId, senderId, receiverId, text, isRead, recipeId);
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public List<UUID> getMessageIdsByUserId(UUID userId) {
        List<UUID> messages = new ArrayList<>();

        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getUserMessages);
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID messageId = UUID.fromString(rs.getString("id"));
                messages.add(messageId);
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public List<Message> getMessagesByUserId(UUID userId) {
        List<Message> messages = new ArrayList<>();
        List<UUID> messageIds = getMessageIdsByUserId(userId);
        for (UUID uuid : messageIds) {
            messages.add(getMessageById(uuid));
        }
        return messages;
    }

    public boolean checkExistId(UUID id, String tableName) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getInstanceById);
            ps.setString(1, tableName);
            ps.setString(2, id.toString());
            ResultSet rs = ps.executeQuery();
            ps.close();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<Ingredient, Float> getCartByUserId(UUID userId) {
        Map<Ingredient, Float> cart = new HashMap<>();
        Map<UUID, Float> cartIds = getCartIdsByUserId(userId);
        for (UUID ingredientId : cartIds.keySet()) {
            Ingredient ingredient = getIngredientById(ingredientId);
            Float quantity = cartIds.get(ingredientId);
            cart.put(ingredient, quantity);
        }
        return cart;
    }

    public Map<UUID, Float> getCartIdsByUserId(UUID userId) {
        Map<UUID, Float> cartIds = new Hashtable<>();
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getUserCart);
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID ingredientId = UUID.fromString(rs.getString("ingredient_id"));
                Float quantity = rs.getFloat("quantity");
                cartIds.put(ingredientId, quantity);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartIds;
    }

    public String userChangeUsername(UUID userId, String username) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.updateUsername);
            ps.setString(1, username);
            ps.setString(2, userId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.USER_SET_USERNAME, username);
            } else {
                return String.format(FailMessages.USER_SET_USERNAME_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.USER_SET_USERNAME_FAIL);
        }
    }

    public String userChangeNickname(UUID userId, String nickname) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.updateNickname);
            ps.setString(1, nickname);
            ps.setString(2, userId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.USER_SET_NICKNAME, nickname);
            } else {
                return String.format(FailMessages.USER_SET_NICKNAME_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.USER_SET_NICKNAME_FAIL);
        }
    }

    public String userChangeEmail(UUID userId, String email) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.updateEmail);
            ps.setString(1, email);
            ps.setString(2, userId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.USER_SET_EMAIL, email);
            } else {
                return String.format(FailMessages.USER_SET_EMAIL_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.USER_SET_EMAIL_FAIL);
        }
    }

    public String userChangePassword(UUID userId, String hashedPassword) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.updatePassword);
            ps.setString(1, hashedPassword);
            ps.setString(2, userId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.USER_SET_PASSWORD);
            } else {
                return String.format(FailMessages.USER_SET_PASSWORD_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.USER_SET_PASSWORD_FAIL);
        }
    }

    public void sendMessage(UUID messageId, UUID senderId, UUID receiverId, String message, Boolean isRead,
            UUID recipeId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addMessage);
            ps.setString(1, messageId.toString());
            ps.setString(2, senderId.toString());
            ps.setString(3, receiverId.toString());
            ps.setString(4, message);
            ps.setBoolean(5, isRead);
            if (recipeId != null) {
                ps.setString(6, recipeId.toString());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
            }
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // ------------------- RECIPE -------------------//

    public List<Recipe> getAllRecipes() {
        List<Recipe> allRecipes = new ArrayList<>();
        // Recipe recipe = new Recipe();

        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getAllRecipes);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID recipeId = UUID.fromString(rs.getString("id"));
                Recipe recipe = getRecipeById(recipeId);

                allRecipes.add(recipe);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allRecipes;
    }

    public List<Recipe> getFavoritesByUserId(UUID userId) {
        List<Recipe> favorites = new ArrayList<>();
        List<UUID> favoriteIds = getFavoriteIdsByUserId(userId);
        for (UUID favoriteId : favoriteIds) {
            favorites.add(getRecipeById(favoriteId));
        }
        return favorites;
    }

    public List<UUID> getFavoriteIdsByUserId(UUID userId) {
        List<UUID> recipes = new ArrayList<>();
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getFavoriteRecipes);
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                recipes.add(UUID.fromString(rs.getString("recipe_id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;

    }

    public Recipe getRecipeById(UUID recipeId) {
        Recipe recipe = null;
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getRecipeById);
            ps.setString(1, recipeId.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("recipe_name");
                InputStream inputStream = rs.getBinaryStream("picture");
                Image picture = new Image(inputStream);
                String description = rs.getString("recipe_description");
                String instructions = rs.getString("instructions");
                UUID author = UUID.fromString(rs.getString("author_id"));
                List<UUID> tags = getTagIdsByRecipeId(recipeId);
                Map<UUID, Float> ingredients = getIngredientIdsByRecipeId(recipeId);
                List<UUID> comments = getCommentIdsByRecipeId(recipeId);
                byte servingSize = rs.getByte("serving_size");
                recipe = new Recipe(recipeId, name, picture, description, instructions, author, tags, ingredients,
                        comments, servingSize);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    public List<Comment> getCommentsByRecipeId(UUID recipeId) {
        List<Comment> comments = new ArrayList<>();
        List<UUID> commentIds = getCommentIdsByRecipeId(recipeId);
        for (UUID commentId : commentIds) {
            comments.add(getCommentById(commentId));
        }
        return comments;
    }

    public List<UUID> getCommentIdsByRecipeId(UUID recipeId) {
        List<UUID> commentIds = new ArrayList<>();
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getRecipeCommentsById);
            ps.setString(1, recipeId.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID commentId = UUID.fromString(rs.getString("id"));
                commentIds.add(commentId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentIds;
    }

    public Comment getCommentById(UUID commentId) {
        Comment comment = null;
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getCommentById);
            ps.setString(1, commentId.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                UUID user = UUID.fromString(rs.getString("user_id"));
                UUID recipeId = UUID.fromString(rs.getString("recipe_id"));
                String text = rs.getString("text");

                comment = new Comment(id, user, recipeId, text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comment;
    }

    public String addComment(UUID commentId, UUID userId, UUID recipeId, String text) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addComment);
            ps.setString(1, commentId.toString());
            ps.setString(2, userId.toString());
            ps.setString(3, recipeId.toString());
            ps.setString(4, text);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return String.format(FailMessages.COMMENT_ADD_FAIL);
        }
        return String.format(SuccessMessages.COMMENT_ADDED);

    }

    public Map<Ingredient, Float> getIngredientsByRecipeId(UUID recipeId) {
        Map<Ingredient, Float> ingredients = new HashMap<>();
        Map<UUID, Float> ingredientIds = getIngredientIdsByRecipeId(recipeId);
        for (UUID ingredientId : ingredientIds.keySet()) {
            Ingredient ingredient = getIngredientById(ingredientId);
            Float quantity = ingredientIds.get(ingredientId);

            ingredients.put(ingredient, quantity);
        }
        return ingredients;
    }

    public Map<UUID, Float> getIngredientIdsByRecipeId(UUID recipeId) {
        Map<UUID, Float> ingredientIds = new Hashtable<>();
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getRecipeIngredientsById);
            ps.setString(1, recipeId.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID ingredientId = UUID.fromString(rs.getString("ingredient_id"));
                Float quantity = rs.getFloat("quantity");

                ingredientIds.put(ingredientId, quantity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredientIds;
    }

    public List<Tag> getTagsByRecipeId(UUID userId, UUID recipeId) {
        List<Tag> tags = new ArrayList<>();
        List<UUID> tagIds = getTagIdsByRecipeId(recipeId);
        for (UUID tagId : tagIds) {
            Tag tag = getTagById(tagId);
            if (tag != null) {
                if (userId.equals(tag.getUser()) || tag.getUser() == null) {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    private List<UUID> getTagIdsByRecipeId(UUID recipeId) {
        List<UUID> tagIds = new ArrayList<>();
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getRecipeTagsById);
            ps.setString(1, recipeId.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID tagId = UUID.fromString(rs.getString("tag_id"));
                tagIds.add(tagId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tagIds;
    }

    public List<Recipe> getRecipesWithNameLike(String name) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getRecipesWithNameLike);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Recipe recipe = getRecipeById(UUID.fromString(rs.getString("id")));
                recipes.add(recipe);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public String addRecipe(UUID recipeId, String name, String picturePath, String description, String instructions,
            byte servingSize,
            UUID authorId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addRecipe);
            InputStream inputStream = getClass().getResourceAsStream(picturePath);
            if (inputStream == null) {
                inputStream = new FileInputStream(picturePath);
            }
            ps.setString(1, recipeId.toString());
            ps.setString(2, name);
            ps.setBlob(3, inputStream);
            ps.setString(4, description);
            ps.setString(5, instructions);
            ps.setByte(6, servingSize);
            ps.setString(7, authorId.toString());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return String.format(SuccessMessages.RECIPE_ADDED);
    }

    // ------------------- INGREDIENT -------------------//

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            useDatabase();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SqlQueries.getAllIngredients);

            while (rs.next()) {
                UUID ingredientId = UUID.fromString(rs.getString("id"));
                ingredients.add(getIngredientById(ingredientId));
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public Ingredient getIngredientById(UUID ingredientId) {
        Ingredient ingredient = null;
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getIngredientById);
            ps.setString(1, ingredientId.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("ingredient_name");
                String unit = rs.getString("unit");

                ingredient = new Ingredient(ingredientId, name, unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredient;
    }

    public List<Ingredient> getIngredientsWithNameLike(String name) {
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getIngredientsWithNameLike);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = getIngredientById(UUID.fromString(rs.getString("id")));
                ingredients.add(ingredient);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    // ------------------- MESSAGE -------------------//
    public String removeMessageById(UUID messageId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.removeMessageById);
            ps.setString(1, messageId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.MESSAGE_DELETED);
            } else {
                return String.format(FailMessages.MESSAGE_DELETE_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.MESSAGE_DELETE_FAIL);
        }
    }

    // ------------------- TAG -------------------//
    public List<Tag> getAllTags(UUID userId) {
        List<Tag> tags = new ArrayList<>();
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getAllTags);
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID tagId = UUID.fromString(rs.getString("id"));
                tags.add(getTagById(tagId));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public Tag getTagById(UUID tagId) {
        Tag tag = null;
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getTagById);
            ps.setString(1, tagId.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("tag_name");
                UUID userId = null;
                if (rs.getString("user_id") != null) {
                    userId = UUID.fromString(rs.getString("user_id"));
                }
                tag = new Tag(tagId, name, userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tag;
    }

    public String removeRecipeById(UUID recipeId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.removeRecipeWithId);
            ps.setString(1, recipeId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.RECIPE_DELETED);
            } else {
                return String.format(FailMessages.RECIPE_DELETE_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.RECIPE_DELETE_FAIL);
        }
    }

    public String addTag(UUID tagId, String name, UUID userId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addTag);
            ps.setString(1, tagId.toString());
            ps.setString(2, name);
            if (userId != null) {
                ps.setString(3, userId.toString());
            } else {
                ps.setNull(3, java.sql.Types.VARCHAR);
            }

            ps.execute();
            ps.close();
            return String.format(SuccessMessages.TAG_ADDED);
        } catch (SQLException e) {
            e.printStackTrace();
            return String.format(FailMessages.TAG_ADD_FAIL);
        }
    }

    public String addRecipeTag(UUID recipeId, UUID tagId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addRecipeTag);
            ps.setString(1, recipeId.toString());
            ps.setString(2, tagId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.RECIPE_TAG_ADDED);
            } else {
                return String.format(FailMessages.RECIPE_TAG_ADD_FAIL);
            }
        } catch (Exception e) {
            return String.format(FailMessages.RECIPE_TAG_ADD_FAIL);
        }
    }

    public String addRecipeIngredient(UUID recipeId, UUID ingredientId, Float quantity) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addRecipeIngredient);
            ps.setString(1, recipeId.toString());
            ps.setString(2, ingredientId.toString());
            ps.setFloat(3, quantity);

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.RECIPE_INGREDIENT_ADDED);
            } else {
                return String.format(FailMessages.RECIPE_INGREDIENT_ADD_FAIL);
            }
        } catch (Exception e) {
            return String.format(FailMessages.RECIPE_INGREDIENT_ADD_FAIL);
        }
    }

    public String addIngredient(UUID ingredientId, String name, String unit) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addIngredient);
            ps.setString(1, ingredientId.toString());
            ps.setString(2, name);
            ps.setString(3, unit);

            ps.execute();
            ps.close();
            return String.format(SuccessMessages.INGREDIENT_ADDED);
        } catch (SQLException e) {
            e.printStackTrace();
            return String.format(FailMessages.INGREDIENT_ADD_FAIL);
        }

    }

    public String addRecipeToPlan(UUID userId, UUID recipeId, Date date) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addRecipeToPlan);
            ps.setString(1, userId.toString());
            ps.setString(2, recipeId.toString());
            ps.setDate(3, date);

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.PLAN_ADDED_RECIPE);
            } else {
                return String.format(FailMessages.PLAN_ADD_RECIPE_FAIL);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return String.format(FailMessages.PLAN_ADD_RECIPE_FAIL);
        }
    }

    public String removeRecipeFromPlan(UUID userId, UUID recipeId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.removeRecipeFromPlan);
            ps.setString(1, userId.toString());
            ps.setString(2, recipeId.toString());

            int result = ps.executeUpdate();
            ps.close();
            if (result != 0) {
                return String.format(SuccessMessages.PLAN_REMOVED_RECIPE);
            } else {
                return String.format(FailMessages.PLAN_REMOVE_RECIPE_FAIL);
            }

        } catch (Exception e) {
            return String.format(FailMessages.PLAN_REMOVE_RECIPE_FAIL);
        }
    }

    public List<Tag> getTagsWithNameLike(UUID userId, String name) {
        List<Tag> tags = new ArrayList<>();
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getTagsWithNameLikeAndNoUser);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tag tag = getTagById(UUID.fromString(rs.getString("id")));
                tags.add(tag);
            }
            ps = conn.prepareStatement(SqlQueries.getTagsWithNameLike);
            ps.setString(1, "%" + name + "%");
            ps.setString(2, userId.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                Tag tag = getTagById(UUID.fromString(rs.getString("id")));
                tags.add(tag);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public String removeCommentById(UUID commentId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.removeCommentWithId);
            ps.setString(1, commentId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.COMMENT_DELETED);
            } else {
                return String.format(FailMessages.COMMENT_DELETE_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.COMMENT_DELETE_FAIL);
        }
    }

    public String editComment(UUID commentId, String commentText) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.editComment);
            ps.setString(1, commentText);
            ps.setString(2, commentId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.COMMENT_EDITED);
            } else {
                return String.format(FailMessages.COMMENT_EDIT_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.COMMENT_EDIT_FAIL);
        }
    }

    public String addToCart(UUID userId, UUID ingredientId, Float amount) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.addIngredientToCart);
            ps.setString(1, userId.toString());
            ps.setString(2, ingredientId.toString());
            ps.setFloat(3, amount);
            ps.execute();
            ps.close();
            return String.format(SuccessMessages.CART_ADDED_INGREDIENT);
        } catch (SQLException e) {
            return String.format(FailMessages.CART_ADD_INGREDIENT_FAIL);
        }
    }

    public String removeFromCart(UUID userId, UUID ingredientId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.removeIngredientFromCart);
            ps.setString(1, userId.toString());
            ps.setString(2, ingredientId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.CART_REMOVED_INGREDIENT);
            } else {
                return String.format(FailMessages.CART_REMOVE_INGREDIENT_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.CART_REMOVE_INGREDIENT_FAIL);
        }

    }

    public String addRecipeIngredients(UUID recipeId, Map<Ingredient, Float> selectedIngredients) {
        try {
            useDatabase();
            for (Entry<Ingredient, Float> ingredient : selectedIngredients.entrySet()) {
                PreparedStatement ps = conn.prepareStatement(SqlQueries.addRecipeIngredient);
                ps.setString(1, recipeId.toString());
                ps.setString(2, ingredient.getKey().getId().toString());
                ps.setFloat(3, ingredient.getValue());

                ps.execute();
                ps.close();
            }
            return String.format(SuccessMessages.RECIPE_INGREDIENT_ADDED);
        } catch (SQLException e) {
            e.printStackTrace();
            return String.format(FailMessages.RECIPE_INGREDIENT_ADD_FAIL);
        }

    }

    public String removeRecipeTagById(UUID recipeId, UUID tagId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.removeRecipeTagById);
            ps.setString(1, recipeId.toString());
            ps.setString(2, tagId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.RECIPE_TAG_REMOVED);
            } else {
                return String.format(FailMessages.RECIPE_TAG_REMOVE_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.RECIPE_TAG_REMOVE_FAIL);
        }

    }

    public List<UUID> getAllTagsIdsByRecipeId(UUID recipeId) {
        List<UUID> recipeIds = new ArrayList<>();
        // try {
        // useDatabase();
        // PreparedStatement ps = conn.prepareStatement(SqlQueries.getUserCart);
        // ps.setString(1, userId.toString());
        // ResultSet rs = ps.executeQuery();

        // while (rs.next()) {
        // UUID ingredientId = UUID.fromString(rs.getString("ingredient_id"));
        // Float quantity = rs.getInt("quantity");
        // cartIds.put(ingredientId, quantity);
        // }
        // ps.close();
        // rs.close();
        // } catch (SQLException e) {
        // e.printStackTrace();
        // }
        return recipeIds;
    }

    public List<UUID> getTagIdsByUserIdAndRecipeId(UUID userId, UUID recipeId) {
        List<UUID> allRecipeTagsIds = new ArrayList<>();
        List<Tag> allRecipeTags = getTagsByRecipeId(userId, recipeId);
        for (Tag tag : allRecipeTags) {
            allRecipeTagsIds.add(tag.getId());
        }
        return allRecipeTagsIds;
    }

    public String editRecipeName(UUID recipeId, String name) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.editRecipeName);
            ps.setString(1, name);
            ps.setString(2, recipeId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.RECIPE_SET_NAME);
            } else {
                return String.format(FailMessages.RECIPE_SET_NAME_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.RECIPE_SET_NAME_FAIL);
        }
    }

    public String editRecipeImage(UUID recipeId, String picturePath) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.editRecipeImage);
            InputStream inputStream = getClass().getResourceAsStream(picturePath);
            if (inputStream == null) {
                inputStream = new FileInputStream(picturePath);
            }
            ps.setBlob(1, inputStream);
            ps.setString(2, recipeId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.RECIPE_SET_PICTURE);
            } else {
                return String.format(FailMessages.RECIPE_SET_PICTURE_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.RECIPE_SET_PICTURE_FAIL);
        } catch (FileNotFoundException e) {
            return String.format(FailMessages.RECIPE_SET_PICTURE_FAIL);
        }
    }

    public String editRecipeDescription(UUID recipeId, String description) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.editRecipeDescription);
            ps.setString(1, description);
            ps.setString(2, recipeId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.RECIPE_SET_DESC);
            } else {
                return String.format(FailMessages.RECIPE_SET_DESC_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.RECIPE_SET_DESC_FAIL);
        }
    }

    public String editRecipeInstructions(UUID recipeId, String instructions) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.editRecipeInstructions);
            ps.setString(1, instructions);
            ps.setString(2, recipeId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.RECIPE_SET_INSTRUCTIONS);
            } else {
                return String.format(FailMessages.RECIPE_SET_INSTRUCTIONS_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.RECIPE_SET_INSTRUCTIONS_FAIL);
        }
    }

    public String editRecipeServingSize(UUID recipeId, byte servingSize) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.editRecipeServeSize);
            ps.setByte(1, servingSize);
            ps.setString(2, recipeId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.RECIPE_SET_SERVING_SIZE);
            } else {
                return String.format(FailMessages.RECIPE_SET_SERVING_SIZE_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.RECIPE_SET_SERVING_SIZE_FAIL);
        }
    }

    public String removeRecipeIngredientsByRecipeId(UUID recipeId) {
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.removeRecipeIngredientsByRecipeId);
            ps.setString(1, recipeId.toString());

            int result = ps.executeUpdate();
            ps.close();

            if (result != 0) {
                return String.format(SuccessMessages.RECIPE_DELETED_INGREDIENTS);
            } else {
                return String.format(FailMessages.RECIPE_DELETE_INGREDIENTS_FAIL);
            }
        } catch (SQLException e) {
            return String.format(FailMessages.RECIPE_DELETE_INGREDIENTS_FAIL);
        }
    }

    public Tag getTagByName(UUID userId, String name) {
        Tag tag = null;
        try {
            useDatabase();
            PreparedStatement ps = conn.prepareStatement(SqlQueries.getTagByNameAndNoUser);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UUID tagId = UUID.fromString(rs.getString("id"));
                tag = new Tag(tagId, name, userId);
            }
            else {
                ps = conn.prepareStatement(SqlQueries.getTagByName);
                ps.setString(1, name);
                ps.setString(2, userId.toString());
                rs = ps.executeQuery();
                if (rs.next()) {
                    UUID tagId = UUID.fromString(rs.getString("id"));
                    tag = new Tag(tagId, name, userId);    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tag;

    }
}