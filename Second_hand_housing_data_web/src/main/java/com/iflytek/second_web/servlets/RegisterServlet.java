package com.iflytek.second_web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflytek.second_web.service.IUserService;
import com.iflytek.second_web.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 注册通常不需要处理GET请求，可以返回错误或重定向到注册页面
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method not supported for registration");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码格式
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        // 获取请求参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String gender = req.getParameter("gender");

        // 创建响应结果Map
        Map<String, Object> responseMap = new HashMap<>();

        // 基本参数校验
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            responseMap.put("register", -2);  // -2表示参数不完整
            responseMap.put("message", "用户名和密码不能为空");
        }
        // 检查是否为admin用户（根据需求可以保留或移除）
        else if ("admin".equals(username)) {
            responseMap.put("register", -1);  // -1表示用户名已存在
            responseMap.put("message", "该用户名已被注册");
        }
        else {
            try {
                // 调用服务层进行注册
                IUserService userService = new UserServiceImpl();
                int rowsAffected = userService.register(username, password, gender);

                if (rowsAffected > 0) {
                    responseMap.put("register", 1);   // 1表示注册成功
                    responseMap.put("message", "注册成功");
                } else {
                    responseMap.put("register", 0);   // 0表示注册失败
                    responseMap.put("message", "注册失败，请稍后重试");
                }
            } catch (Exception e) {
                responseMap.put("register", -3);     // -3表示系统错误
                responseMap.put("message", "系统错误: " + e.getMessage());
            }
        }

        // 将结果转为JSON并返回
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), responseMap);
    }
}