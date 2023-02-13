import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 14776
 * Date: 2022-11-25
 * Time: 14:06
 */
public class DBUtil { //单例模式封装数据源对象
    private static volatile DataSource dataSource = null;

    private static DataSource getDataSource(){
        if(dataSource == null){
            synchronized (DBUtil.class){
                if(dataSource == null){
                    dataSource = new MysqlDataSource();
                    ((MysqlDataSource)dataSource).setURL("jdbc:mysql://127.0.0.1:3306/java105_blog_system?characterEncoding=utf8&useSSL=false");
                    ((MysqlDataSource)dataSource).setUser("root");
                    ((MysqlDataSource)dataSource).setPassword("");
                }
            }
        }
        return dataSource;
    }

    //把建立连接的方法封装一下
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    //封装一个关闭资源的函数
    public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet){
        //这几个资源的关闭顺序是和它的创建顺序相反的
        //关闭资源的几个if分开写，不要套在一个try catch中 分开写一个抛异常不影响其他的资源关闭，但是写在一起一个抛异常就会直接走catch就会影响其他的资源关闭
        //影响资源关闭，可能就会造成了资源泄露 资源泄露是很严重的错误
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
