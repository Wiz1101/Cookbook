package util.exceptions.recipe;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidRecipeIngredientsCountException extends Exception {
  public InvalidRecipeIngredientsCountException() {
    super(String.format(FailMessages.RECIPE_INVALID_INGREDIENTS_COUNT));
  }
}