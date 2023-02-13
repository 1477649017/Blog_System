import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 14776
 * Date: 2022-11-28
 * Time: 18:05
 */
@WebServlet("/userInfo")
public class UserInfoServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取用户信息
        String blogId = req.getParameter("blogId");
        if(blogId == null){
            //列表页来访问的 直接从会话中获取信息
            getUserInfoFromSession(req,resp);
        }else{
            //详情页 通过博客id来检索到userId 进一步查询到用户信息 博客是谁写的，那么详情页用户信息展示的就是谁
            getUserInfoFromDB(req,resp,Integer.parseInt(blogId));
        }

    }

    private void getUserInfoFromDB(HttpServletRequest req, HttpServletResponse resp, int blogId) throws IOException {
        BlogDao blogDao = new BlogDao();
        Blog blog = blogDao.selectOne(blogId);
        if(blog == null){
            resp.setStatus(404);
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前博客不存在!");
            return;
        }

        //查询到了相关的博客
        UserDao userDao = new UserDao();
        User user = userDao.selectById(blog.getUserId());
        if(user == null){
            resp.setStatus(404);
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前用户不存在!");
            return;
        }
        user.setPassword("");//把密码信息置为空之后再进行写回
        resp.setContentType("application/json;charset=utf8");
        resp.getWriter().write(objectMapper.writeValueAsString(user));
    }

    private void getUserInfoFromSession(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null){
            resp.setStatus(403);
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录!");
            return;
        }

        User user = (User) httpSession.getAttribute("user");
        if(user == null){
            resp.setStatus(403);
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录!");
            return;
        }

        //user获取到了 但是建议把password干掉后再传回前端
        user.setPassword("");
        resp.setContentType("application/json;charset=utf8");
        resp.getWriter().write(objectMapper.writeValueAsString(user));
    }
}
