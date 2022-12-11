package util.common;


import javafx.scene.control.TextArea;
import models.entities.Comment;
import models.entities.Ingredient;
import models.entities.Message;
import models.entities.Recipe;
import models.entities.Tag;
import javafx.scene.image.ImageView;


public interface UserListener {
    public void onClickListener(Recipe recipe);
    public void descriptionListener(Recipe recipe);
    public void favClickListener(Recipe recipe, ImageView imageView);
    public void ingredientClickListener(Ingredient ingredient, ImageView ingredientButton);
    public void tagClickListener(Tag tag); //(Tag tag, ImageView tagButton)

    public void recipeEntered(Recipe recipe, TextArea textArea);
    public void recipeExited(Recipe recipe, TextArea textArea);

    public void shareTheRecipeListener();
    public void openRecipeListener(Recipe recipe);
    
    public void replyMsgListener(Message message);
    public void removeMsgListener(Message message);
    public void closeMsgListener();
    public void closeOpenForDetailed();
    public void closeSendMsgListener();
    public void closeCartListener();
    public void removeRecipeListener();
    public void removeCommentListener(Comment comment);
    public void editCommentListener(Comment comment, String text);
    public void addIngredientToCart(Ingredient ingredient, Float quantity);
    public void removeIngredientFromCart(Ingredient ingredient);
    public void editRecipeListener(Recipe recipe);
    
    public void addTagToRecipe(Tag tag); // (Tag tag, ImageView tagButton)
    public void removeTagFromRecipe(Tag tag);
    public void emptyCart();


}
