package com.iflytek.second_web.servlets;

import com.iflytek.second_web.service.impl.DistrictServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/district/stats")
public class DistrictServlet extends HttpServlet {
    private DistrictServiceImpl districtService = new DistrictServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String jsonData = districtService.getDistrictStatsJson();

        try (PrintWriter out = resp.getWriter()) {
            out.write(jsonData);
        }
    }
}