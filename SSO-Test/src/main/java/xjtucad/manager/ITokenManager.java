package xjtucad.manager;

import java.util.Map;

public interface ITokenManager {

    void storeUserInfo(Map<String, String> userInfo, String token);

    Map<String, String> getUserInfo(String token);

    boolean containToken(String token);

    void removeUserInfo(String token);

}
