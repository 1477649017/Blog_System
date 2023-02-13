/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 14776
 * Date: 2022-11-25
 * Time: 14:53
 */
//        +--------+----------+----------+
//        | userId | username | password |
//        +--------+----------+----------+
//        |      1 | 张三      | 1234     |
//        |      2 | 李四      | 8888     |
//        +--------+----------+----------+
public class User {
    private int userId;
    private String username;
    private String password;

    //提供get set方法

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
