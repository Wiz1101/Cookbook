package util.exceptions.tag;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidTagNameLengthException extends Exception {
  public InvalidTagNameLengthException() {
    super(String.format(FailMessages.TAG_INVALID_NAME_LENGTH));
  }
}