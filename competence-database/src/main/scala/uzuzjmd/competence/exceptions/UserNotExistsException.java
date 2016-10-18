package uzuzjmd.competence.exceptions;

import uzuzjmd.competence.persistence.dao.User;

public class UserNotExistsException extends Exception {


    private final User user;

    public UserNotExistsException() {
        user = null;
    }

    public UserNotExistsException(User user) {
        this.user = user;
    }

    @Override
    public String getMessage() {
        return "User does not exist in Database" + user.getName() +" : " +  user.getId();
    }
}
