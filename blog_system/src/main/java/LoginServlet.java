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
 * Time: 16:02
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        //首先获取一下username password
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if(username == null || username.equals("") || password == null || password.equals("")){
            //前端在输入是没有做判空操作的，所以这里要检验
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("用户名或者密码为空,登录失败!");
            return;
        }

        //根据username进行查询数据库
        UserDao userDao = new UserDao();
        User user = userDao.selectByName(username);
        if(user == null || !user.getPassword().equals(password)){
            //没有查询到相关用户，或者说密码对不上
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("用户名或者密码错误,登录失败!");
            return;
        }

        //走到这里说明用户名密码都正确 分配会话
        HttpSession httpSession = req.getSession(true);
        //在会话中保存一下用户信息 后续再访问页面就可以维护登录状态 也就知道是谁在访问了
        httpSession.setAttribute("user",user);

        //构造响应 重定向
        resp.setStatus(302);
        resp.sendRedirect("blog_list.html");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //使用这个方法来判定登录状态
        //获取当前会话
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null){
            //会话不存在 也即是未登录状态
            resp.setStatus(403);
            return;
        }

        //获取一下user对象
        //这里涉及到后面的注销中的情况 可能会有会话存在但是User对象没有
        User user = (User) httpSession.getAttribute("user");
        if(user == null){
            //会话存在 user对象不存在 一样是未登录
            resp.setStatus(403);
            return;
        }

        //走到这里就是登录了
        resp.setStatus(200);
    }
}
