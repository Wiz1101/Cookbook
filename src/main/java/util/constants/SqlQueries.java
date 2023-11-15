package util.constants;

public class SqlQueries {
        public static final String dropDatabase = "DROP DATABASE Cookbook"; // modified here
        public static final String setMaxAllowedPackage = "SET GLOBAL max_allowed_packet=10000000"; // modified here
        public static final String useDatabase = "USE Cookbook"; // modified here

        // CREATE -------------
        public static final String createDatabase = "CREATE DATABASE Cookbook"; // modified here
        public static final String createTableUserTags = "CREATE TABLE users_tags ("
                        + "id CHAR(38) NOT NULL PRIMARY KEY,"
                        + "tag_name VARCHAR(30) NOT NULL,"
                        + "user_id CHAR(38)"
                        + ");";

        public static final String createTableUsers = "CREATE TABLE users ("
                        + "id CHAR(38) NOT NULL PRIMARY KEY,"
                        + "username VARCHAR(30) NOT NULL,"
                        + "display_name VARCHAR(30),"
                        + "email VARCHAR(50) NOT NULL,"
                        + "password CHAR(64) NOT NULL"
                        + ");";

        public static final String createTableMessages = "CREATE TABLE messages ("
                        + "id CHAR(38) NOT NULL PRIMARY KEY,"
                        + "sender_id CHAR(38) NOT NULL "
                        + "REFERENCES users(id),"
                        + "receiver_id CHAR(38) NOT NULL "
                        + "REFERENCES users(id),"
                        + "message_text VARCHAR(300) NOT NULL,"
                        + "is_read BOOL NOT NULL,"
                        + "recipe_id CHAR(38)"
                        + ");";

        public static final String createTableIngredients = "CREATE TABLE ingredients ("
                        + "id CHAR(38) NOT NULL PRIMARY KEY,"
                        + "ingredient_name VARCHAR(60) NOT NULL,"
                        + "unit VARCHAR(20) NOT NULL"
                        + ");";

        public static final String createTableUserIngredients = "CREATE TABLE users_ingredients ("
                        + "user_id CHAR(38) NOT NULL "
                        + "REFERENCES users(id),"
                        + "ingredient_id CHAR(38) NOT NULL "
                        + "REFERENCES ingredients(id),"
                        + "quantity FLOAT NOT NULL,"
                        + "PRIMARY KEY (user_id, ingredient_id)"
                        + ");";

        public static final String createTableRecipes = "CREATE TABLE recipes ("
                        + "id CHAR(38) NOT NULL PRIMARY KEY,"
                        + "recipe_name VARCHAR(60) NOT NULL,"
                        + "picture LONGBLOB NOT NULL,"
                        + "recipe_description VARCHAR(1500) NOT NULL,"
                        + "instructions VARCHAR(4000) NOT NULL,"
                        + "serving_size TINYINT NOT NULL,"
                        + "author_id CHAR(38) NOT NULL "
                        + "REFERENCES users(id)"
                        + ");";

        public static final String createTableComments = "CREATE TABLE comments ("
                        + "id CHAR(38) NOT NULL PRIMARY KEY,"
                        + "user_id CHAR(38) NOT NULL "
                        + "REFERENCES users(id),"
                        + "recipe_id CHAR(38) NOT NULL "
                        + "REFERENCES recipes(id),"
                        + "text VARCHAR(300) NOT NULL"
                        + ");";

        public static final String createTableRecipeIngredients = "CREATE TABLE recipes_ingredients ("
                        + "recipe_id CHAR(38) NOT NULL "
                        + "REFERENCES recipes(id),"
                        + "ingredient_id CHAR(38) NOT NULL "
                        + "REFERENCES ingredients(id),"
                        + "quantity FLOAT NOT NULL,"
                        + "PRIMARY KEY (recipe_id, ingredient_id)"
                        + ");";

        public static final String createTableRecipeTags = "CREATE TABLE recipes_tags ("
                        + "recipe_id CHAR(38) NOT NULL "
                        + "REFERENCES recipes(id),"
                        + "tag_id CHAR(38) NOT NULL "
                        + "REFERENCES users_tags(id),"
                        + "PRIMARY KEY(recipe_id, tag_id)"
                        + ");";

        public static final String createTableUsersFavorites = "CREATE TABLE users_favorites ("
                        + "user_id CHAR(38) NOT NULL "
                        + "REFERENCES users(id),"
                        + "recipe_id CHAR(38) NOT NULL "
                        + "REFERENCES recipes(id),"
                        + "PRIMARY KEY (user_id, recipe_id)"
                        + ");";

        public static final String createTableWeeklyPlans = "CREATE TABLE weekly_plans ("
                        + "user_id CHAR(38) NOT NULL "
                        + "REFERENCES users(id),"
                        + "recipe_id CHAR(38) "
                        + "REFERENCES recipes(id),"
                        + "date DATE NOT NULL,"
                        + "PRIMARY KEY (user_id, recipe_id)"
                        + ");";

        public static final String createTableUsersRecipeTags = "CREATE TABLE users_recipes_tags ("
                        + "user_id CHAR(38) NOT NULL "
                        + "REFERENCES users(id),"
                        + "recipe_id CHAR(38) NOT NULL "
                        + "REFERENCES recipes(id),"
                        + "tag_id CHAR(38) NOT NULL "
                        + "REFERENCES tags(id),"
                        + "PRIMARY KEY (user_id, recipe_id, tag_id)"
                        + ");";

        // INSERT------------------------------------------------------
        public static final String addRecipe = "INSERT INTO recipes (\n" +
                        "id, recipe_name, picture, recipe_description, instructions, serving_size, author_id) \n" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        public static final String addUser = "INSERT INTO users (\n" +
                        "id, username, display_name, email, password) \n" +
                        "VALUES (?, ?, ?, ?, ?)";

        public static final String addTag = "INSERT INTO users_tags (\n" +
                        "id, tag_name, user_id) \n" +
                        "VALUES (?, ?, ?)";

        public static final String addMessage = "INSERT INTO messages (\n" +
                        "id, sender_id, receiver_id, message_text, is_read, recipe_id) \n" +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        public static final String addComment = "INSERT INTO comments (\n" +
                        "id, user_id, recipe_id, text) \n" +
                        "VALUES (?, ?, ?, ?)";

        public static final String addIngredient = "INSERT INTO ingredients (\n" +
                        "id, ingredient_name, unit) \n" +
                        "VALUES (?, ?, ?)";

        public static final String addRecipeTag = "INSERT INTO recipes_tags (recipe_id, tag_id) VALUES (?, ?)";

        public static final String addRecipeIngredient = "INSERT INTO recipes_ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)";

        public static final String addRecipeToPlan = "INSERT INTO weekly_plans (user_id, recipe_id, date) VALUES (?, ?, ?)";

        public static final String addIngredientToCart = "INSERT INTO users_ingredients (user_id, ingredient_id, quantity) VALUES (?, ?, ?)";
        
        
        // SELECT -------------
        public static final String getUserByCredentials = "SELECT * FROM users WHERE username = ? AND password = ?";

        public static final String getUserByNickname = "SELECT * FROM users WHERE display_name = ?";

        public static final String getUserByUsername = "SELECT * FROM users WHERE username = ?";

        public static final String getUserByEmail = "SELECT * FROM users WHERE email = ?";

        public static final String getUserCart = "SELECT * FROM users_ingredients WHERE user_id = ?";

        public static final String getIngredientById = "SELECT * FROM ingredients WHERE id = ?";

        public static final String getUserMessages = "SELECT * FROM messages WHERE receiver_id = ?";

        public static final String getMessageById = "SELECT * FROM messages WHERE id = ?";
        
        public static final String getUserById = "SELECT * FROM users WHERE id = ?";

        public static final String getUserWeeklyList = "SELECT * FROM weekly_plans WHERE user_id = ?";

        public static final String getInstanceById = "SELECT * FROM ? WHERE id = ?";

        public static final String getUserFavorites = "SELECT * FROM users_favorites WHERE user_id = ?";

        public static final String getUserRecipes = "SELECT * FROM recipes WHERE author_id = ?";
        
        public static final String getAllRecipes = "SELECT * FROM recipes";

        public static final String getRecipeById = "SELECT * FROM recipes WHERE id = ?";

        public static final String getRecipeTagsById = "SELECT * FROM recipes_tags WHERE recipe_id = ?";

        public static final String getRecipeIngredientsById = "SELECT * FROM recipes_ingredients WHERE recipe_id = ?";

        public static final String getRecipeCommentsById = "SELECT * FROM comments WHERE recipe_id = ?";

        public static final String getFavoriteRecipes = "SELECT * FROM users_favorites WHERE user_id = ?";
        
        public static final String getAllUsers = "SELECT * FROM users";

        public static final String getAllIngredients = "SELECT * FROM ingredients";

        public static final String removeUserWithId = "DELETE FROM users WHERE id = ?";

        public static final String removeRecipeWithId = "DELETE FROM recipes WHERE id = ?";

        public static final String removeRecipeFromPlan = "DELETE FROM weekly_plans WHERE user_id = ? AND recipe_id = ?";

        public static final String removeCommentWithId = "DELETE FROM comments WHERE id = ?";

        public static final String removeIngredientFromCart = "DELETE FROM users_ingredients WHERE user_id = ? AND ingredient_id = ?";
        
        public static final String removeRecipeTagById = "DELETE FROM recipes_tags WHERE recipe_id = ? AND tag_id = ?";
        
        public static final String removeRecipeIngredientsByRecipeId = "DELETE FROM recipes_ingredients WHERE recipe_id = ?";
        
        public static final String getAllTags = "SELECT * FROM users_tags WHERE user_id IS NULL OR user_id = ?";

        public static final String getTagByNameAndNoUser = "SELECT * FROM users_tags WHERE tag_name = ? AND user_id IS NULL";

        public static final String getTagByName = "SELECT * FROM users_tags WHERE tag_name = ? AND user_id = ?";

        public static final String getTagById = "SELECT * FROM users_tags WHERE id = ?";

        public static final String getCommentById = "SELECT * FROM comments WHERE id = ?";

        public static final String getRecipesWithNameLike = "SELECT * FROM recipes WHERE recipe_name LIKE ?";

        public static final String getUsersLike = "SELECT * FROM users WHERE username LIKE ? OR display_name LIKE ? OR email LIKE ?";

        public static final String getIngredientsWithNameLike = "SELECT * FROM ingredients WHERE ingredient_name LIKE ?";

        public static final String getTagsWithNameLikeAndNoUser = "SELECT * FROM users_tags WHERE tag_name LIKE ? AND user_id IS NULL";

        public static final String getTagsWithNameLike = "SELECT * FROM users_tags WHERE tag_name LIKE ? AND user_id = ?";


        // UPDATE -------------
        public static final String updateUsername = "UPDATE users SET username = ? WHERE id = ?";

        public static final String updateNickname = "UPDATE users SET display_name = ? WHERE id = ?";

        public static final String updateEmail = "UPDATE users SET email = ? WHERE id = ?";

        public static final String updatePassword = "UPDATE users SET password = ? WHERE id = ?";

        public static final String editComment = "UPDATE comments SET text = ? WHERE id = ?";

        public static final String removeMessageById = "DELETE FROM messages WHERE id = ?";

        public static final String addRecipeFavorite = "INSERT INTO users_favorites (\n" +
                        "user_id, recipe_id) \n" +
                        "VALUES (?, ?)";

        public static final String validateUniqueCredentials = "SELECT * FROM users WHERE username = ? OR email = ?";

        public static final String removeRecipeFavorite = "DELETE FROM users_favorites WHERE user_id = ? AND recipe_id = ?";

        public static final String editRecipeName = "UPDATE recipes SET recipe_name = ? WHERE id = ?";

        public static final String editRecipeDescription = "UPDATE recipes SET recipe_description = ? WHERE id =?";

        public static final String editRecipeInstructions = "UPDATE recipes SET instructions = ? WHERE id = ?";

        public static final String editRecipeServeSize = "UPDATE recipes SET serving_size = ? WHERE id = ?";

        public static final String editRecipeImage = "UPDATE recipes SET picture = ? WHERE id = ?";
}
