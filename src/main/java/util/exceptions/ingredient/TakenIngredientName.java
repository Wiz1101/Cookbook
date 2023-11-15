package util.exceptions.ingredient;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class TakenIngredientName extends Exception {
  public TakenIngredientName(String name) {
    super(String.format(FailMessages.INGREDIENT_NAME_TAKEN));
  }
}