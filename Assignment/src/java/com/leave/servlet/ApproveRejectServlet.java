package com.leave.servlet;

import com.leave.dao.LeaveRequestDAO;
import com.leave.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class ApproveRejectServlet extends HttpServlet {
    private LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || (user.getRoleId() != 2 && user.getRoleId() != 3)) {
            response.sendRedirect("login");
            return;
        }
        request.setAttribute("requestId", request.getParameter("id"));
        request.getRequestDispatcher("approve_reject.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || (user.getRoleId() != 2 && user.getRoleId() != 3)) {
            response.sendRedirect("login");
            return;
        }

        int requestId = Integer.parseInt(request.getParameter("requestId"));
        String status = request.getParameter("status");
        String processReason = request.getParameter("processReason");

        leaveRequestDAO.updateLeaveRequestStatus(requestId, status, user.getId(), processReason);
        response.sendRedirect("leave_list");
    }
}