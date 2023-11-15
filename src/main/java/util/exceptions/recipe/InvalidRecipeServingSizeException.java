package util.exceptions.recipe;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidRecipeServingSizeException extends Exception {
  public InvalidRecipeServingSizeException() {
    super(String.format(FailMessages.RECIPE_INVALID_SERVING_SIZE));
  }
}