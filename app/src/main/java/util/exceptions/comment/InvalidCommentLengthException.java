package util.exceptions.comment;

import util.constants.FailMessages;

/**
 * Exception for invalid member email.
 */
public class InvalidCommentLengthException extends Exception {
  public InvalidCommentLengthException() {
    super(String.format(FailMessages.COMMENT_INVALID_TEXT_LENGTH));
  }
}