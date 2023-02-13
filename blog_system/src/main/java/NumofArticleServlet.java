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
 * Date: 2023-01-03
 * Time: 15:26
 */
@WebServlet("/articleNum")
public class NumofArticleServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //这个方法用来接收更新用户文章数量信息的请求
        String blogId = req.getParameter("blogId");//这个blogId存不存在就具体看query string存不存在了
        //展示用户文章数量 可能是来自两个页面的请求 如果是列表页 那么就是登录用户 直接从会话获取信息
        //如果是详情页 那么就是文章作者信息 从博客间接去获取信息
        //列表页没有blogId的信息 而详情页会有
        if(blogId == null){
            getNumFromSession(req,resp);
        }else{
            getNumFromDB(req,resp,blogId);
        }
    }

    private void getNumFromDB(HttpServletRequest req, HttpServletResponse resp, String blogId) throws IOException {
        //这里就要根据blogId 间接获取到userId来进行检索了
        BlogDao blogDao = new BlogDao();
        Blog blog = blogDao.selectOne(Integer.parseInt(blogId));//获取指定的博客
        int userId = blog.getUserId();//获取到该博客的userId信息
        //然后就可以通过userId进行检索
        int count = blogDao.getArticleNum(userId);
        resp.setContentType("application/json;charset=utf8");//指定一下响应的格式
        resp.getWriter().println("{\"count\":" + count + "}");//构造写入body的数据 键值对的形式
    }

    private void getNumFromSession(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null){
            resp.setStatus(403);
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录!");
            return;
        }

        User user = (User) httpSession.getAttribute("user");//获取会话中的user对象
        if(user == null){
            resp.setStatus(403);
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录!");
            return ;
        }
        //走到这里说明是登录好了的
        //开始根据user对象中的userId检索文章数量
        int userId = user.getUserId();
        BlogDao blogDao = new BlogDao();
        int count = blogDao.getArticleNum(userId);
        resp.setContentType("application/json;charset=utf8");//指定一下响应的格式
        resp.getWriter().println("{\"count\":" + count + "}");//构造写入body的数据 键值对的形式
    }
}
