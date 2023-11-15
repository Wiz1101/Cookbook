package util.exceptions.user;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class TakenUsernameException extends Exception {
  public TakenUsernameException(String username) {
    super(String.format(FailMessages.USER_NAME_TAKEN, username));
  }
}