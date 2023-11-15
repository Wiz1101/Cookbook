package util.exceptions.user;

import util.constants.FailMessages;

/**
 * Exception for invalid member name.
 */
public class InvalidPasswordComplexityException extends Exception {
  public InvalidPasswordComplexityException() {
    super(String.format(FailMessages.USER_INVALID_PASSWORD_COMPLEXITY));
  }
}