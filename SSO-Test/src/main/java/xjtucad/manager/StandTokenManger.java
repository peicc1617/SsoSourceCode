package xjtucad.manager;

import xjtucad.model.ExpiryMap;

import java.util.Map;

public class StandTokenManger implements ITokenManager {

    protected Map<String,Map<String,String>> tokenMap = new ExpiryMap<String,Map<String,String>>();
    @Override
    public void storeUserInfo(Map<String,String> userInfo, String token) {
        tokenMap.put(token,userInfo);
        //数据修改标识符
    }

    @Override
    public Map<String, String> getUserInfo(String token) {
        return tokenMap.get(token);
    }

    @Override
    public boolean containToken(String token) {
        return tokenMap.containsKey(token);
    }

    @Override
    public void removeUserInfo(String token) {
        tokenMap.remove(token);
    }
}
