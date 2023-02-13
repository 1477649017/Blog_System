import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 14776
 * Date: 2022-11-25
 * Time: 15:16
 */
//DAO Data Access Object 访问数据库的操作都可以使用这个类完成
    //注意Connection一定是java.sql.Connection
public class BlogDao {

    //插入一个博客 -- 发布博客
    public void insert(Blog blog){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //获取连接
            connection = DBUtil.getConnection();
            //构造sql
            //now()是sql中的一个函数 获取当前时间
            String sql = "insert into blog values(null, ?, ?, now(), ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,blog.getTitle());
            preparedStatement.setString(2,blog.getContent());
            preparedStatement.setInt(3,blog.getUserId());
            //执行sql
            int ret = preparedStatement.executeUpdate();
            if(ret != 1){
                System.out.println("博客插入失败!");
            }else{
                System.out.println("博客插入成功!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //在finally中关闭资源才是最保险的
            DBUtil.close(connection,preparedStatement,resultSet);
        }
    }

    //根据博客id显示指定的博客详情 -- 博客详情页
    public Blog selectOne(int blogId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            String sql = "select * from blog where blogId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,blogId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                blog.setContent(resultSet.getString("content"));
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                blog.setUserId(resultSet.getInt("userId"));
                return blog;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return null;
    }

    //删除指定的博客 -- 删除博客
    public void deleteBlog(int blodId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            String sql = "delete from blog where blogId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,blodId);
            int ret = preparedStatement.executeUpdate();
            if(ret != 1){
                System.out.println("博客删除失败!");
            }else{
                System.out.println("博客删除成功!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
    }

    //查询所有博客 -- 展示博客列表
    public List<Blog> selectAll(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Blog> list = new ArrayList<>();

        try {
            connection = DBUtil.getConnection();
            //按照发布时间降序排列，才能保证最新发布的博客在页面的最上面先显示
            String sql = "select * from blog order by postTime desc";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                String content = resultSet.getString("content");
                //注意，这是博客列表页，我们不需要看全你的博客详细内容，只需要显示一些摘要就可，直接显示所有正文，那么就太多了，页面也放不下多少
                if(content.length() > 100){
                    //如果长度大于100
                    content = content.substring(0,100) + "....";//截取一部分
                }
                blog.setContent(content);
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                blog.setUserId(resultSet.getInt("userId"));
                list.add(blog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return list;
    }

    public int getArticleNum(int userId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;
        try{
            //获取连接
            connection = DBUtil.getConnection();
            String sql = "select count(*) from blog where userId = ?";//根据userID来统计该用户的文章数量
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                count = resultSet.getInt(1);//获取结果
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return count;//返回结果
    }
}
