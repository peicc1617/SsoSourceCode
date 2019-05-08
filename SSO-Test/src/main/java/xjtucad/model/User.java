package xjtucad.model;

import java.io.Serializable;

public class User implements Serializable{
    /**
     * 用户ID
     */
    private long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 组织
     */
    private String domain;

    private String permission;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phoneNumber;

    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 工号
     */
    private String jobNumber;
    /**
     * 组织
     */
    private String domainName;


    public User() {
    }

    public User(long id, String username, String password, String domain, String permission, String email, String phoneNumber, String nickName, String jobNumber, String domainName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.domain = domain;
        this.permission = permission;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
        this.jobNumber = jobNumber;
        this.domainName = domainName;
    }

    public User(long id, String username, String domain, String permission, String email, String phoneNumber, String nickName, String jobNumber) {
        this.id = id;
        this.username = username;
        this.domain = domain;
        this.permission = permission;
        this.email = email;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.jobNumber = jobNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain == null ? null : domain.trim();
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission == null ? null : permission.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", domain='" + domain + '\'' +
                ", permission='" + permission + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nickName='" + nickName + '\'' +
                ", jobNumber='" + jobNumber + '\'' +
                ", domainName='" + domainName + '\'' +
                '}';
    }
}