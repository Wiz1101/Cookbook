package util.exceptions.user;

import util.constants.FailMessages;

/**
 * Exception for invalid member name.
 */
public class InvalidPasswordLengthException extends Exception {
  public InvalidPasswordLengthException() {
    super(String.format(FailMessages.USER_INVALID_PASSWORD_LENGTH));
  }
}