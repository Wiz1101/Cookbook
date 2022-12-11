package util.exceptions.user;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class UserReceiverCannotBeSenderException extends Exception {
  public UserReceiverCannotBeSenderException() {
    super(String.format(FailMessages.USER_RECEIVER_IS_SENDER));
  }
}