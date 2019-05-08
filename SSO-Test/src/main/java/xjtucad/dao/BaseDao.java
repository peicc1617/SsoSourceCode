package xjtucad.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BaseDao {
    //数据库地址“jdbc:mysql://服务器域名:端口号/数据库名称”
    private String url = "jdbc:mysql://localhost:3306/innovation_user_info?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT";
    //用户名
    private String user = "xjtucad";
    //用户密码
    private String pwd = "xjtucad";
    //数据库链接对象
    private java.sql.Connection conn;
    //数据库命令执行对象
    private PreparedStatement pstmt;
    //数据库返回结果
    private java.sql.ResultSet rs;
    //静态代码块
    static{
        //1、加载驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //2、创建连接
    private void getConnection(){
        if(conn == null){
            try {
                Context initCtx = new InitialContext();
                if(initCtx == null ){
                    throw new Exception("No Context");
                }
                Context ctx = (Context) initCtx.lookup("java:comp/env");
                DataSource ds = (DataSource)ctx.lookup("jdbc/userInfo");  //项目名要与context.xml里面的对应
                if(ds != null){
                    conn = ds.getConnection();
                    System.out.println("使用context.xml配置文件数据库");
                }
            } catch (SQLException e) {
                System.out.println("数据库链接异常，使用程序默认配置");
                conn = getConnectionDefault();
                e.printStackTrace();
            } catch (NamingException e) {
                System.out.println("context文件数据库配置有误，使用程序默认配置");
                conn = getConnectionDefault();
                e.printStackTrace();
            } catch (Exception e) {
                conn = getConnectionDefault();
                e.printStackTrace();
            }
        }
    }

    private Connection getConnectionDefault(){
        try {
            return DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //执行读操作方法
    public java.sql.ResultSet executeQuery(String query, List<Object> params){
        getConnection();
        try {
            System.out.println(conn.isValid(100));
            //3、创建命令执行对象
            pstmt = conn.prepareStatement(query);
            //4、执行
            if(params!=null && params.size()>0){
                for(int i=0;i<params.size();i++){
                    pstmt.setObject(i+1, params.get(i));
                }
            }
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    //执行写操作方法
    public int executeUpdate(String query,
                             List<Object> params){
        int result = 0;
        getConnection();
        try {
            //3、创建命令执行对象
            pstmt = conn.prepareStatement(query);
            //4、执行
            if(params!=null && params.size()>0){
                for(int i=0;i<params.size();i++){
                    pstmt.setObject(i+1, params.get(i));
                }
            }
            //5、处理结果
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            //6、释放资源
            this.close();
        }
        return result;
    }


    //关闭资源
    public void close(){
        try {
            if(rs!=null){
                rs.close();
                rs = null;
            }
            if(pstmt!=null){
                pstmt.close();
                pstmt = null;
            }
            if(conn!=null){
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
