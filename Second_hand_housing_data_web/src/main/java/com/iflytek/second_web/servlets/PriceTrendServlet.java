package com.iflytek.second_web.servlets;

import com.iflytek.second_web.service.impl.PriceTrendServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/price/trend")
public class PriceTrendServlet extends HttpServlet {
    private PriceTrendServiceImpl priceTrendService = new PriceTrendServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String jsonData = priceTrendService.getPriceTrendsJson();

        try (PrintWriter out = resp.getWriter()) {
            out.write(jsonData);
        }
    }
}