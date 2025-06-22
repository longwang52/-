package com.iflytek.second_web.servlets;

import com.iflytek.second_web.domain.Community;
import com.iflytek.second_web.service.impl.CommunityServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/community/top")
public class CommunityServlet extends HttpServlet {
    private CommunityServiceImpl communityService = new CommunityServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            int topN = 20;
            String nParam = req.getParameter("n");

            if (nParam != null && !nParam.isEmpty()) {
                try {
                    topN = Integer.parseInt(nParam);
                    if (topN <= 0) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                "参数n必须大于0");
                        return;
                    }
                } catch (NumberFormatException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "参数n必须是有效整数");
                    return;
                }
            }

            List<Community> communities = communityService.getTopCommunities(topN);

            try (PrintWriter out = resp.getWriter()) {
                out.println("[");
                for (int i = 0; i < communities.size(); i++) {
                    Community c = communities.get(i);
                    out.println("  {");
                    out.println("    \"district\": \"" + c.getDistrict() + "\",");
                    out.println("    \"community\": \"" + c.getCommunity() + "\",");
                    out.println("    \"houseCount\": " + c.getHouseCount() + ",");
                    out.println("    \"avgPrice\": \"" + c.getAvgPrice() + "\",");
                    out.println("    \"avgPricePerSqm\": \"" + c.getAvgPricePerSqm() + "\",");
                    out.println("    \"avgBuildYear\": \"" + c.getAvgBuildYear() + "\"");
                    out.print("  }");
                    if (i < communities.size() - 1) out.println(",");
                }
                out.println("\n]");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"error\":\"服务器内部错误: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}