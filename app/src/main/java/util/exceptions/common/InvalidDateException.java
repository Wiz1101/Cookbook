package util.exceptions.common;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidDateException extends Exception {
  public InvalidDateException() {
    super(String.format(FailMessages.INVALID_DATE));
  }
}