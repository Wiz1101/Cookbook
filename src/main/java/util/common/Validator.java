package util.common;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.constants.Variables;
import util.exceptions.common.InvalidCountException;
import util.exceptions.common.InvalidInstanceException;
import util.exceptions.common.InvalidLengthException;
import util.exceptions.user.InvalidEmailException;
import util.exceptions.user.InvalidNicknameLengthException;
import util.exceptions.user.InvalidPasswordComplexityException;
import util.exceptions.user.InvalidPasswordLengthException;
import util.exceptions.user.InvalidUserNameLengthException;
import util.exceptions.user.TakenEmailException;
import util.exceptions.user.TakenNicknameException;
import util.exceptions.user.TakenUsernameException;

public class Validator {
    private static DbContext dbContext = new DbContext(Variables.DATABASE_PORT, Variables.DATABASE_USER, Variables.DATABASE_PASS);
    /**
     * Checks if a string is within given length.
     *
     * @param string    string
     * @param minLength min length
     * @param maxLength max length
     * @throws InvalidLengthException out of range
     */
    public static void validateStringLength(String string, int minLength, int maxLength)
            throws InvalidLengthException {
        if (string.length() < minLength
                || string.length() > maxLength) {
            throw new InvalidLengthException();
        }
    }

    /**
     * Checks if count is in a given range.
     *
     * @param count    current count
     * @param minCount min count
     * @param maxCount max count
     * @throws InvalidCountException out of range
     */
    public static void validateCount(int count, int minCount, int maxCount)
            throws InvalidCountException {
        if (count < minCount
                || count > maxCount) {
            throw new InvalidCountException();
        }
    }

    /**
     * Checks if a user exists in the database.
     *
     * @param entity user
     * @throws InvalidInstanceException not in database
     */
    public static void validateUser(UUID id) throws InvalidInstanceException {
        if (dbContext.getUserById(id) == null) {
            throw new InvalidInstanceException();
        }
    }

    public static void validateRecipe(UUID id) throws InvalidInstanceException {
        if (dbContext.getRecipeById(id) == null) {
            throw new InvalidInstanceException();
        }
    }

    public static void validateUsername(String username) throws TakenUsernameException, InvalidUserNameLengthException {
        try {
            Validator.validateStringLength(username, Variables.MIN_USER_NAME_LENGTH, Variables.MAX_USER_NAME_LENGTH);
        } catch (InvalidLengthException e) {
            throw new InvalidUserNameLengthException();
        }

        if (dbContext.getUserByUsername(username) != null) {
            throw new TakenUsernameException(username);
        }
    }

    public static void validateNickname(String nickname) throws InvalidNicknameLengthException, TakenNicknameException {
        try {
            Validator.validateStringLength(nickname, Variables.MIN_USER_NICK_LENGTH, Variables.MAX_USER_NICK_LENGTH);
        } catch (InvalidLengthException e) {
            throw new InvalidNicknameLengthException();
        }

        if (dbContext.getUserByNickname(nickname) != null) {
            throw new TakenNicknameException(nickname);
        }

    }

    public static void validateEmail(String email) throws TakenEmailException, InvalidEmailException {
        Pattern pattern = Pattern.compile(Variables.USER_EMAIL_REGEX,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find() || email == null) {
            throw new InvalidEmailException();
        }

        if (dbContext.getUserByEmail(email) != null) {
            throw new TakenEmailException();
        }
    }

    public static void validatePassword(String password) throws InvalidPasswordLengthException, InvalidPasswordComplexityException {
        try {
            validateStringLength(password, Variables.MIN_PASSWORD_LENGTH, Variables.MAX_PASSWORD_LENGTH);
        } catch (InvalidLengthException e) {
            throw new InvalidPasswordLengthException();
        }
        validatePasswordComplexity(password);
    }

    private static void validatePasswordComplexity(String password) throws InvalidPasswordComplexityException {
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        for (int i = 0; i < password.length(); i++) { // Check for lowercase, uppercase and digit
            Character currChar = password.charAt(i);
            if (Character.isLowerCase(currChar)
                    && !hasLowerCase) {
                hasLowerCase = true;
            } else if (Character.isUpperCase(currChar)
                    && !hasUpperCase) {
                hasUpperCase = true;
            } else if (Character.isDigit(currChar)
                    && !hasDigit) {
                hasDigit = true;
            }
            if (hasLowerCase && hasUpperCase && hasDigit) {
                break;
            }

            }
            if (!hasLowerCase
            || !hasUpperCase
            || !hasDigit) {
        throw new InvalidPasswordComplexityException();
        }
    }

}
