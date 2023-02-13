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
 * Date: 2022-11-29
 * Time: 13:05
 */
@WebServlet("/delete")
public class BlogDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //首先判定登录状态
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null){
            resp.setContentType("text/html;charset=utf8");
            resp.setStatus(403);
            resp.getWriter().write("当前未登录，不能删除博客!");
            return;
        }

        User user = (User) httpSession.getAttribute("user");
        if(user == null){
            resp.setContentType("text/html;charset=utf8");
            resp.setStatus(403);
            resp.getWriter().write("当前未登录，不能删除博客!");
            return;
        }

        //然后获取参数
        String blogId = req.getParameter("blogId");
        if(blogId == null){
            resp.setContentType("text/html;charset=utf8");
            resp.setStatus(404);
            resp.getWriter().write("blogId不存在，无法进行删除!");
            return;
        }
        //通过blogId找到对应的博客
        BlogDao blogDao = new BlogDao();
        Blog blog = blogDao.selectOne(Integer.parseInt(blogId));
        if(blog == null){
            resp.setContentType("text/html;charset=utf8");
            resp.setStatus(404);
            resp.getWriter().write("当前博客不存在，无法进行删除!");
            return;
        }

        //如果上面都正常 找到了对应的博客
        //判断当前登录用户是不是文章的作者
        if(user.getUserId() != blog.getUserId()){
            //user中的是登录用户 blog中的是文章作者
            //说明现在你看的是别人的博客 不能进行删除
            resp.setContentType("text/html;charset=utf8");
            resp.setStatus(403);
            resp.getWriter().write("无法删除他人的博客!");
            return;
        }

        //开始进行删除
        blogDao.deleteBlog(Integer.parseInt(blogId));
        //重定向到列表页
        resp.sendRedirect("blog_list.html");
    }
}
