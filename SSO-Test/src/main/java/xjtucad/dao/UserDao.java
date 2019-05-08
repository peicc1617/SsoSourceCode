package xjtucad.dao;

import xjtucad.model.LoginResult;
import xjtucad.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDao extends BaseDao {

    /**
     * 获取除了当前用户以外的所有用户
     *
     * @param curUsername
     * @return
     */
    public List<User> getAllUserExceptMe(String curUsername) {
        String query = "select * from user_info where username != ?";
        ResultSet rs = this.executeQuery(query, Collections.singletonList((Object) curUsername));
        List<User> users = new ArrayList<User>();
        try {
            users = getUserListFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
        return users;
    }

    /**
     * 获取全部用户
     *
     * @return
     */
    public List<User> getAllUser() {
        String query = "select * from user_info";
        ResultSet rs = this.executeQuery(query, null);
        List<User> users = new ArrayList<User>();
        try {
            users = getUserListFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
        return users;
    }

    /**
     * 验证用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @return
     */
    public Map<String, String> getUser(String username, String password) {
        String query = "SELECT id,username,domain,permission,email,phoneNumber,nickName,jobNumber FROM user_info WHERE username = ? and password = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(username);
        params.add(password);
        ResultSet rs = this.executeQuery(query, params);
        Map<String, String> userInfo = new HashMap<String, String>();
        try {
            userInfo = getMapFromRs(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
        return userInfo;
    }

    /**
     * 验证该用户名是否存在
     *
     * @param username 用户名
     * @return
     * @throws SQLException
     */
    public LoginResult validateUsername(String username) {
        String query = "select 1 from user_info where username = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(username);
        ResultSet rs = this.executeQuery(query, params);
        try {
            if (rs.next())
                return LoginResult.SUCCESS;
            return LoginResult.USERNAME_ERROR;
        } catch (SQLException e) {
            e.printStackTrace();
            return LoginResult.INTERNAL_ERROR;
        } finally {
            this.close();
        }
    }


    public List<User> getUserListFromResultSet(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<User>();
        for (User user = getUserForResultSet(rs);user!=null;user=getUserForResultSet(rs)){
            users.add(user);
        }
        return users;
    }

    public User getUserForResultSet(ResultSet rs) throws SQLException {
        if(rs.next()){
            return new User(rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("domain"),
                    rs.getString("permission"),
                    rs.getString("email"),
                    rs.getString("phoneNumber"),
                    rs.getString("nickName"),
                    rs.getString("jobNumber"));
        }else {
            return null;
        }
    }

    public Map<String,String> getMapFromRs(ResultSet rs) throws SQLException {
        Map<String,String> map = new HashMap<String,String>();
        if(rs.next()){
            map.put("id",rs.getString("id"));
            map.put("username",rs.getString("username"));
            map.put("domain",rs.getString("domain"));
            map.put("permission",rs.getString("permission"));
            map.put("email",rs.getString("email"));
            map.put("phoneNumber",rs.getString("phoneNumber"));
            map.put("nickName",rs.getString("nickName"));
            map.put("jobNumber",rs.getString("jobNumber"));
            return map;
        }else {
            return null;
        }
    }
}
