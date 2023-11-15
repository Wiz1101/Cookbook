package util.common;

import models.entities.User;

public interface AdminListener {
    public void editUser(User user, String username, String nickname, String email, String password);
    public void removeUser(User user);
    
}
