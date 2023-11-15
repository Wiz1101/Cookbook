package util.exceptions.user;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidEmailException extends Exception {
  public InvalidEmailException() {
    super(String.format(FailMessages.USER_INVALID_EMAIL));
  }
}