package util.exceptions.user;

import util.constants.FailMessages;

/**
 * Exception for invalid member name.
 */
public class InvalidUserNameLengthException extends Exception {
  public InvalidUserNameLengthException() {
    super(String.format(FailMessages.USER_INVALID_NAME_LENGTH));
  }
}