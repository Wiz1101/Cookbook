package util.exceptions.recipe;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidRecipeDescriptionLengthException extends Exception {
  public InvalidRecipeDescriptionLengthException() {
    super(String.format(FailMessages.RECIPE_INVALID_DESCRIPTION_LENGTH));
  }
}