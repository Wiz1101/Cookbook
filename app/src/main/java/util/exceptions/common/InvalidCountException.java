package util.exceptions.common;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidCountException extends Exception {
  public InvalidCountException() {
    super(String.format(FailMessages.INVALID_COUNT));
  }
}