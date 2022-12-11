package util.exceptions.common;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidInstanceException extends Exception {
  public InvalidInstanceException() {
    super(String.format(FailMessages.INVALID_COUNT));
  }
}