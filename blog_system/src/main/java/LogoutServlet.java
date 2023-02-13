import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
 * Created with IntelliJ IDEA.
 * Description:
 * User: 14776
 * Date: 2022-11-29
 * Time: 11:18
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession(false);//获取一下当前的会话
        if(httpSession == null){
            resp.setStatus(403);
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("当前未登录,无法注销!");
            return ;
        }

        httpSession.removeAttribute("user");//直接把会话中的user对象删除掉
        resp.sendRedirect("blog_login.html");
    }
}
