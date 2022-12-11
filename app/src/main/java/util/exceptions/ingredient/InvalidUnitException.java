package util.exceptions.ingredient;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidUnitException extends Exception {
  public InvalidUnitException() {
    super(String.format(FailMessages.MESSAGE_INVALID_TEXT_LENGTH));
  }
}