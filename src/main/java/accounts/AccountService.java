package accounts;
import java.util.HashMap;
import java.util.Map;
//Класс, содержащий методы для взаимодействия с пользовательскими данными.
public class AccountService {

    private final Map<String, accounts.UserProfile> loginToProfile;
    private final Map<String, accounts.UserProfile> sessionIdToProfile;

    public AccountService() {
        loginToProfile = new HashMap<>();
        sessionIdToProfile = new HashMap<>();
    }
    public void addNewUser(accounts.UserProfile userProfile) {
        loginToProfile.put(userProfile.getLogin(), userProfile);
    }
    public accounts.UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    public void addSession(String sessionId, accounts.UserProfile userProfile) {
        sessionIdToProfile.put(sessionId, userProfile);
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }

}

