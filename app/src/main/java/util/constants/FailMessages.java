package util.constants;

public class FailMessages {
        //
        // USER
        //
        public static final String USER_NOT_EXIST = "User does not exist.";
        public static final String USER_INVALID_NAME_LENGTH = "Username must be between "
                        + Variables.MIN_USER_NAME_LENGTH + " and " + Variables.MAX_USER_NAME_LENGTH
                        + " characters long.\n";

        public static final String USER_NAME_TAKEN = "Username %s is already taken.\n";

        public static final String USER_INVALID_PASSWORD_LENGTH = "Password must be between "
                        + Variables.MIN_PASSWORD_LENGTH + " and " + Variables.MAX_PASSWORD_LENGTH
                        + " characters long.\n";

        public static final String USER_INVALID_PASSWORD_COMPLEXITY = "Password must include "
                        + "at least one uppercase letter,\n one lowercase letter and one digit.\n";

        public static final String USER_INVALID_EMAIL = "Please enter a valid email.\n";

        public static final String USER_EMAIL_TAKEN = "Email is already taken.\n";

        public static final String USER_INVALID_NICKNAME_LENGTH = "Nickname must be between "
                        + Variables.MIN_USER_NICK_LENGTH + " and "
                        + Variables.MAX_USER_NICK_LENGTH + " characters long.\n";
        public static final String USER_NICK_TAKEN = "Nickname %s is already taken.\n";

        public static final String USER_RECIPE_NOT_FAVORITE = "Recipe is not in favorites.\n";

        public static final String USER_ADD_FAIL = "Could not add user.\n";
        public static final String USER_DELETE_FAIL = "Could not delete user.\n";
        public static final String USER_SET_USERNAME_FAIL = "Could not change username.\n";
        public static final String USER_SET_NICKNAME_FAIL = "Could not change user nickname.\n";
        public static final String USER_SET_EMAIL_FAIL = "Could not change user email.\n";
        public static final String USER_SET_PASSWORD_FAIL = "Could not change user password.\n";
        public static final String USER_ADD_FAVORITE_RECIPE_FAIL = "Could not add recipe to favorites.\n";
        public static final String USER_RECEIVER_IS_SENDER = "Cannot send a message to yourself.\n";
        //
        // TAG
        //
        public static final String TAG_INVALID_NAME_LENGTH = "Tag name must be between "
                        + Variables.MIN_TAG_NAME_LENGTH + " and " + Variables.MAX_TAG_NAME_LENGTH
                        + " characters long.\n";
        public static final String TAG_ADD_FAIL = "Could not add tag.\n";

        //
        // RECIPE
        //
        public static final String RECIPE_INVALID_NAME_LENGTH = "Recipe name must be between "
                        + Variables.MIN_RECIPE_NAME_LENGTH + " and " +
                        Variables.MAX_RECIPE_NAME_LENGTH + " characters long.\n";

        public static final String RECIPE_INVALID_DESCRIPTION_LENGTH = "Recipe description must be "
                        + "between " + Variables.MIN_RECIPE_DESC_LENGTH + " and "
                        + Variables.MAX_RECIPE_DESC_LENGTH + " characters long.\n";

        public static final String RECIPE_INVALID_INSTRUCTIONS_LENGTH = "Recipe instructions must be "
                        + "between " + Variables.MIN_RECIPE_INSTRUCTIONS_LENGTH + " and "
                        + Variables.MAX_RECIPE_INSTRUCTIONS_LENGTH + " characters long.\n";

        public static final String RECIPE_INVALID_INGREDIENTS_COUNT = "Recipe must have between "
                        + Variables.MIN_RECIPE_INGREDIENTS + " and "
                        + Variables.MAX_RECIPE_INGREDIENTS + " ingredients.\n";

        public static final String RECIPE_INVALID_TAGS_COUNT = "Recipe must have between "
                        + Variables.MIN_RECIPE_TAGS + " and "
                        + Variables.MAX_RECIPE_TAGS + " tags.\n";

        public static final String RECIPE_INVALID_SERVING_SIZE = "Recipe serving size must be "
                        + "even number between " + Variables.MIN_RECIPE_SERVING_SIZE + " and "
                        + Variables.MAX_RECIPE_SERVING_SIZE + ".\n";

        public static final String RECIPE_NOT_EXIST = "Recipe does not exist.\n";
        public static final String RECIPE_DELETE_FAIL = "Could not delete recipe.\n";
        public static final String RECIPE_TAG_ADD_FAIL = "Could not add tag to recipe.\n";
        public static final String RECIPE_INGREDIENT_ADD_FAIL = "Could not add ingredient to recipe.\n";
        public static final String RECIPE_EDIT_FAIL = "Could not edit recipe.\n";
        public static final String RECIPE_TAG_REMOVE_FAIL = "Could not remove tag from recipe.\n";
        public static final String RECIPE_SET_NAME_FAIL = "Could not set recipe name.\n";
        public static final String RECIPE_SET_DESC_FAIL = "Coult not set recipe description.\n";
        public static final String RECIPE_SET_INSTRUCTIONS_FAIL = "Could not set recipe instructions.\n";
        public static final String RECIPE_SET_SERVING_SIZE_FAIL = "Could not set recipe serving size.\n";
        public static final String RECIPE_SET_PICTURE_FAIL = "Could not set recipe picture.\n";
        public static final String RECIPE_DELETE_INGREDIENTS_FAIL = "Could not remove recipe ingredients.\n";
        
        //
        // MESSAGE
        //
        public static final String MESSAGE_INVALID_TEXT_LENGTH = "Message must be between "
                        + Variables.MIN_MESSAGE_TEXT_LENGTH + " and "
                        + Variables.MAX_MESSAGE_TEXT_LENGTH + " characters long.\n";

        public static final String MESSAGE_INVALID_RECEIVER = "Could not send message.\n";

        public static final String MESSAGE_INVALID_SENDER = "Could not send message.\n";
        public static final String MESSAGE_DELETE_FAIL = "Could not delete message.\n";
        public static final String MESSAGE_SEND_FAIL = "Could not send message.\n";

        //
        // INGREDIENT
        //
        public static final String INGREDIENT_INVALID_NAME_LENGTH = "Ingredient name must be "
                        + "between " + Variables.MIN_INGREDIENT_NAME_LENGTH + " and "
                        + Variables.MAX_INGREDIENT_NAME_LENGTH + " characters long.\n";

        public static final String INGREDIENT_NOT_EXIST = "Ingredient %s does not exist.\n";
        public static final String INGREDIENT_UNIT_NOT_EXIST = "Unit %s does not exist.\n";
        public static final String INGREDIENT_NAME_TAKEN = "Ingredient %s already exists.\n";
        public static final String INGREDIENT_ADD_FAIL = "Could not add ingredient.\n";

        //
        // COMMENT
        //
        public static final String COMMENT_INVALID_TEXT_LENGTH = "Comment should be between "
                        + Variables.MIN_COMMENT_TEXT_LENGTH + " and "
                        + Variables.MAX_COMMENT_TEXT_LENGTH + " characters long.\n";
        public static final String COMMENT_ADD_FAIL = "Could not add comment.\n";
        public static final String COMMENT_DELETE_FAIL = "Could not delete comment.\n";
        public static final String COMMENT_EDIT_FAIL = "Could not edit comment.\n";

        //
        // PLAN
        // 
        public static final String PLAN_ADD_RECIPE_FAIL = "Could not add recipe to plan.\n";
        public static final String PLAN_REMOVE_RECIPE_FAIL = "Could not remove recipe from plan.\n";
        
        //
        // CART
        //
        public static final String CART_ADD_INGREDIENT_FAIL = "Could not add ingredient to cart.\n";
        public static final String CART_REMOVE_INGREDIENT_FAIL = "Could not remove ingredient from cart.\n";

        //
        // MISC
        //
        public static final String INVALID_ALGORITHM = "There was an error. "
                        + "Could not save password.\n";

        public static final String INVALID_LENGTH = "Invalid length.\n";
        public static final String INVALID_COUNT = "Invalid count.\n";
        public static final String INVALID_INSTANCE = "Invalid instance.\n";
        public static final String INVALID_DATE = "Date cannot be before today.\n";
        

}
