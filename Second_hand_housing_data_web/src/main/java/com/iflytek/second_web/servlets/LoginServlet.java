package com.iflytek.second_web.servlets;

import com.iflytek.second_web.domain.User;
import com.iflytek.second_web.service.IUserService;
import com.iflytek.second_web.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        // 获取用户提交的用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("userpwd");

        // 进行身份验证
        // 先获取 业务层，进行业务处理
        IUserService userService = new UserServiceImpl();
        User user = userService.login(username, password);
        if (user != null) {
            // 清除 request中的域对象保存的错误信息
            request.removeAttribute("serverTip");

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(request.getContextPath() + "/dashboard.html");
        }else{
            doGet(request, resp);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getServletContext().getContextPath();
        // flag = 3 表示用户名或密码错
        resp.setCharacterEncoding("UTF-8");
        req.setAttribute("serverTip", "用户名或密码错");
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
