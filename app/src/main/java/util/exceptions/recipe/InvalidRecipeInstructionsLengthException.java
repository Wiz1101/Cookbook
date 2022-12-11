package util.exceptions.recipe;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidRecipeInstructionsLengthException extends Exception {
  public InvalidRecipeInstructionsLengthException() {
    super(String.format(FailMessages.RECIPE_INVALID_INSTRUCTIONS_LENGTH));
  }
}