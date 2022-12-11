package util.exceptions.message;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidMessageTextException extends Exception {
  public InvalidMessageTextException() {
    super(String.format(FailMessages.MESSAGE_INVALID_TEXT_LENGTH));
  }
}