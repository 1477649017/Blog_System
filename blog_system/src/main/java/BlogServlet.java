import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 14776
 * Date: 2022-11-25
 * Time: 18:45
 */
@WebServlet("/blog")
public class BlogServlet extends HttpServlet {
    private static ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogDao blogDao = new BlogDao();
        resp.setContentType("application/json;charset=utf8");
        //现在有个问题就是博客详情页 博客列表页都要使用get请求，所以在这个方法里面需要我们去做出区分 区分的依据就是query string
        String blogId = req.getParameter("blogId");
        if(blogId == null){
            //就是博客列表页
            List<Blog> blogs = blogDao.selectAll();//获取所有的博客
            resp.getWriter().write(objectMapper.writeValueAsString(blogs));
        }else{
            //博客详情页
            Blog blog = blogDao.selectOne(Integer.parseInt(blogId));
            resp.getWriter().write(objectMapper.writeValueAsString(blog));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //按理说可以进入编辑页就已经是登录状态了 为什么还要判断登录状态
        //因为如果利用第三方软件例如postman直接构造请求是可以绕过你的页面刷新判断登录状态的代码的
        //并且后面获取到userId也要利用会话

        //首先获取会话判断登录状态
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null){
            resp.setContentType("text/html;charset=utf8");
            resp.setStatus(403);
            resp.getWriter().write("当前未登录，不能发布博客!");
            return;
        }

        //获取了会话 然后拿到user对象
        User user = (User) httpSession.getAttribute("user");
        if(user == null){
            resp.setContentType("text/html;charset=utf8");
            resp.setStatus(403);
            resp.getWriter().write("当前未登录，不能发布博客!");
            return;
        }

        //一切正常 就可以获取参数了
        //取参数之前一定要设置编码格式 不然接收中文会有问题
        req.setCharacterEncoding("utf8");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        //然后那数据插入到数据库中
        BlogDao blogDao = new BlogDao();
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setUserId(user.getUserId());
        blogDao.insert(blog);

        //插入之后重定向到博客列表页
        resp.sendRedirect("blog_list.html");
    }
}
