package services.impl;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import models.entities.Ingredient;
import models.entities.Message;
import models.entities.Recipe;
import models.entities.Tag;
import models.entities.User;
import services.UserService;
import util.common.DbContext;
import util.common.Hasher;
import util.common.SceneContext;
import util.common.Validator;
import util.constants.Variables;
import util.exceptions.comment.InvalidCommentLengthException;
import util.exceptions.common.InvalidLengthException;
import util.exceptions.message.InvalidMessageTextException;
import util.exceptions.user.InvalidEmailException;
import util.exceptions.user.InvalidNicknameLengthException;
import util.exceptions.user.InvalidPasswordComplexityException;
import util.exceptions.user.InvalidPasswordLengthException;
import util.exceptions.user.InvalidUserNameLengthException;
import util.exceptions.user.TakenEmailException;
import util.exceptions.user.TakenNicknameException;
import util.exceptions.user.TakenUsernameException;
import util.exceptions.user.UserReceiverCannotBeSenderException;

public class UserServiceImpl implements UserService {
    private DbContext dbContext;

    public UserServiceImpl() {
        super();
        dbContext = new DbContext(Variables.DATABASE_PORT, Variables.DATABASE_USER, Variables.DATABASE_PASS);
    }

    @Override
    public List<User> getUsers() {
        return dbContext.getAllUsers();
    }

    @Override
    public String addUser(UUID userId, String username, String email, String password)
            throws TakenUsernameException, InvalidUserNameLengthException, TakenEmailException, InvalidEmailException,
            InvalidPasswordLengthException, InvalidPasswordComplexityException, NoSuchAlgorithmException {
        validateUsername(username);
        validateEmail(email);
        validatePassword(password);
        String hashedPassword = Hasher.hashString(password);
        return dbContext.addUser(userId, username, email, hashedPassword);
    }

    @Override
    public String removeUserById(UUID userId) {
        return dbContext.removeUserById(userId);
    }

    @Override
    public String changeUsername(UUID userId, String username)
            throws TakenUsernameException, InvalidUserNameLengthException {
        validateUsername(username);
        User user = dbContext.getUserById(userId);
        user.setUsername(username);
        return dbContext.userChangeUsername(userId, username);
    }

    @Override
    public String changeNickname(UUID userId, String nickname)
            throws InvalidNicknameLengthException, TakenNicknameException {
        validateNickname(nickname);
        User user = dbContext.getUserById(userId);
        user.setNickname(nickname);
        return dbContext.userChangeNickname(userId, nickname);
    }

    @Override
    public String changeEmail(UUID userId, String email) throws TakenEmailException, InvalidEmailException {
        validateEmail(email);
        User user = dbContext.getUserById(userId);
        user.setEmail(email);
        return dbContext.userChangeEmail(userId, email);
    }

    @Override
    public String changePassword(UUID userId, String password)
            throws InvalidPasswordLengthException, InvalidPasswordComplexityException, NoSuchAlgorithmException {
        validatePassword(password);
        User user = dbContext.getUserById(userId);
        String hashedPassword = Hasher.hashString(password);
        user.setPassword(hashedPassword);
        return dbContext.userChangePassword(userId, hashedPassword);

    }

    @Override
    public String sendMessage(UUID messageId, UUID senderId, UUID receiverId, String message, UUID recipeId)
            throws InvalidMessageTextException, UserReceiverCannotBeSenderException {
        validateMessageText(message);
        if (senderId.equals(receiverId)) {
            throw new UserReceiverCannotBeSenderException();
        }
        dbContext.sendMessage(messageId, senderId, receiverId, message, false, recipeId);
        return null;
    }

    @Override
    public String addToCart(UUID userId, UUID ingredientId, Float amount) {
        return dbContext.addToCart(userId, ingredientId, amount);
    }

    @Override
    public String removeFromCart(UUID userId, UUID ingredientId) {
        return dbContext.removeFromCart(userId, ingredientId);
    }

    @Override
    public boolean addToFavorites(UUID userId, UUID recipeId) {
        SceneContext.getUser().addRecipeToFavorites(recipeId);
        return dbContext.addRecipeToFavorites(userId, recipeId);
    }

    @Override
    public boolean removeFromFavorites(UUID userId, UUID recipeId) {
        SceneContext.getUser().removeRecipeFromFavorite(recipeId);
        return dbContext.removeRecipeFromFavorites(userId, recipeId);
    }

    @Override
    public String addToPlan(UUID userId, UUID recipeId, Date date) {
        return dbContext.addRecipeToPlan(userId, recipeId, date);
    }

    @Override
    public String removeFromPlan(UUID userId, UUID recipeId) {
        return dbContext.removeRecipeFromPlan(userId, recipeId);
    }

    @Override
    public String addComment(UUID commentId, UUID userId, UUID recipeId, String commentText) throws InvalidCommentLengthException {
        validateComment(commentText);
        return dbContext.addComment(commentId, userId, recipeId, commentText);
    }

    @Override
    public String editComment(UUID commentId, String commentText) throws InvalidCommentLengthException {
        validateComment(commentText);
        return dbContext.editComment(commentId, commentText);
    }

    @Override
    public String removeComment(UUID commentId) {
        return dbContext.removeCommentById(commentId);
    }

    @Override
    public User loginUser(String username, String password) {
        return dbContext.getUserByCredentials(username, password);
    }

    @Override
    public User getUserById(UUID id) {
        return dbContext.getUserById(id);
    }

    @Override
    public String removeMessageById(UUID messageId) {
        return dbContext.removeMessageById(messageId);
    }

    @Override
    public List<Message> getUserMessagesById(UUID userId) {
        return dbContext.getMessagesByUserId(userId);
    }

    @Override
    public User getUserByNickname(String nickname) {
        return dbContext.getUserByNickname(nickname);
    }

    @Override
    public List<Recipe> getFavoriteRecipes(UUID userId) {
        return dbContext.getFavoritesByUserId(userId);
    }

    @Override
    public Map<Recipe, Date> getWeeklyPlan(UUID userId) {
        return dbContext.getWeeklyListByUserId(userId);
    }

    @Override
    public List<User> getUsersLike(String text) {
        return dbContext.getUsersLike(text);
    }

    @Override
    public void validateUsername(String username) throws TakenUsernameException, InvalidUserNameLengthException {
        Validator.validateUsername(username);
    }

    @Override
    public void validateNickname(String nickname) throws InvalidNicknameLengthException, TakenNicknameException {
        Validator.validateNickname(nickname);
    }

    @Override
    public void validateEmail(String email) throws TakenEmailException, InvalidEmailException {
        Validator.validateEmail(email);
    }

    @Override
    public void validatePassword(String password)
            throws InvalidPasswordLengthException, InvalidPasswordComplexityException {
        Validator.validatePassword(password);
    }

    @Override
    public void validateMessageText(String message) throws InvalidMessageTextException {
        try {
            Validator.validateStringLength(message, Variables.MIN_MESSAGE_TEXT_LENGTH,
                    Variables.MAX_MESSAGE_TEXT_LENGTH);
        } catch (InvalidLengthException e) {
            throw new InvalidMessageTextException();
        }
    }

    @Override
    public Map<Ingredient, Float> getUserCartById(UUID id) {
        return dbContext.getCartByUserId(id);
    }

    private void validateComment(String commentText) throws InvalidCommentLengthException {
        try {
            Validator.validateStringLength(commentText, Variables.MIN_COMMENT_TEXT_LENGTH,
                    Variables.MAX_COMMENT_TEXT_LENGTH);
        } catch (InvalidLengthException e) {
            throw new InvalidCommentLengthException();
        }
    }

    @Override
    public List<Tag> getUserRecipeTags(UUID userId, UUID recipeId) {
        return dbContext.getTagsByRecipeId(userId, recipeId);
    }

}
