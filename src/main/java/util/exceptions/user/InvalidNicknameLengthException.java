package util.exceptions.user;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidNicknameLengthException extends Exception {
  public InvalidNicknameLengthException() {
    super(String.format(FailMessages.USER_INVALID_NICKNAME_LENGTH));
  }
}