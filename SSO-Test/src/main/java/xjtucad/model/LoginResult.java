package xjtucad.model;

public enum LoginResult {
    NO_USERNAME("请输入用户名"),
    NO_PASSWORD("请输入密码"),
    INTERNAL_ERROR("服务器错误"),
    USERNAME_ERROR("用户不存在"),
    PASSWORD_ERROR("密码错误"),
    SUCCESS("登录成功");
    private final String info;
    LoginResult(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return this.info;
    }
}
