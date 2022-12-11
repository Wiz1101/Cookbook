package util.constants;

public class Variables {
    public static final int DATABASE_PORT = 3306;
    public static final String DATABASE_USER = "root";
    public static final String DATABASE_PASS = "root1234";

    public static final String DATABASE_SERVER_URL = "jdbc:mysql://localhost:" + DATABASE_PORT + "/";
    public static final String DATABASE_COOKBOOK_URL = "jdbc:mysql://localhost:" + DATABASE_PORT + "/Cookbook";

    public static final int MIN_USER_NAME_LENGTH = 3; // User
    public static final int MAX_USER_NAME_LENGTH = 25;

    public static final int MIN_USER_NICK_LENGTH = 3;
    public static final int MAX_USER_NICK_LENGTH = 25;

    public static final int MIN_TAG_NAME_LENGTH = 3; // Tag
    public static final int MAX_TAG_NAME_LENGTH = 20;

    public static final int MIN_PASSWORD_LENGTH = 6; // Password
    public static final int MAX_PASSWORD_LENGTH = 25;

    public static final String USER_EMAIL_REGEX = "^[A-Z0-9.]+@[A-Z0-9.]+\\.[A-Z]+$"; // Email
    public static final int MIN_USER_EMAIL_LENGTH = 8;
    public static final int MAX_USER_EMAIL_LENGTH = 50;

    public static final int MIN_INGREDIENT_NAME_LENGTH = 3; // Ingredient
    public static final int MAX_INGREDIENT_NAME_LENGTH = 25;

    public static final int MIN_MESSAGE_TEXT_LENGTH = 1; // Message
    public static final int MAX_MESSAGE_TEXT_LENGTH = 300;

    public static final int MIN_RECIPE_NAME_LENGTH = 3; // Recipe
    public static final int MAX_RECIPE_NAME_LENGTH = 70;
    public static final int MIN_RECIPE_DESC_LENGTH = 10;
    public static final int MAX_RECIPE_DESC_LENGTH = 300;
    public static final int MIN_RECIPE_INSTRUCTIONS_LENGTH = 20;
    public static final int MAX_RECIPE_INSTRUCTIONS_LENGTH = 1000;
    public static final int MIN_RECIPE_INGREDIENTS = 1;
    public static final int MAX_RECIPE_INGREDIENTS = 15;
    public static final int MIN_RECIPE_TAGS = 1;
    public static final int MAX_RECIPE_TAGS = 15;
    public static final int MIN_RECIPE_SERVING_SIZE = 2;
    public static final int MAX_RECIPE_SERVING_SIZE = 16;

    public static final int MIN_COMMENT_TEXT_LENGTH = 10; // Comment
    public static final int MAX_COMMENT_TEXT_LENGTH = 300;
}
