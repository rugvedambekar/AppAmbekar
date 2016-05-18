package ra.appambekar.models.events;

/**
 * Created by rugvedambekar on 16-05-17.
 */
public class LoginEvent {

    public boolean loginSuccess;
    public String failureMsg;

    public LoginEvent(boolean success) {
        loginSuccess = success;
    }

    public LoginEvent(String fMsg) {
        loginSuccess = false;
        failureMsg = fMsg;
    }
}
