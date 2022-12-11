package util.exceptions.common;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidLengthException extends Exception {
  public InvalidLengthException() {
    super(String.format(FailMessages.INVALID_LENGTH));
  }
}