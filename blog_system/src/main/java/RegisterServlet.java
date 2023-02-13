import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 14776
 * Date: 2022-12-01
 * Time: 14:07
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //实现注册功能
        req.setCharacterEncoding("utf8");
        //获取参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirm_password = req.getParameter("confirm_password");
        //前端没有做判断空操作 所以这里需要做判空操作
        if(username == null || username.equals("") || password == null || password.equals("") || confirm_password == null || confirm_password.equals("")){
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("用户名,密码或者确认密码为空,登录失败!");
            return;
        }
        if(username.length() > 6 || !(password.length() >= 6 && password.length() <= 10)){
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("用户名，密码长度要求不符合规范!");
            return ;
        }
        //检测密码与确认密码输入的是否一致
        if(!password.equals(confirm_password)){
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("两次输入的密码不一致，注册失败!");
            return;
        }
        //开始插入数据到数据库
        UserDao userDao = new UserDao();
        userDao.insertUser(username,password);//调用函数插入数据进入数据库
        //设置响应
        resp.sendRedirect("blog_login.html");//相当于刷新一下页面 让用户开始登录
    }
}
