package com.leave.servlet;

import com.leave.dao.LeaveRequestDAO;
import com.leave.model.LeaveRequest;
import com.leave.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class LeaveRequestServlet extends HttpServlet {
    private LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        request.getRequestDispatcher("leave_request.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String reason = request.getParameter("reason");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setUserId(user.getId());
            leaveRequest.setReason(reason);
            leaveRequest.setStartDate(sdf.parse(startDateStr));
            leaveRequest.setEndDate(sdf.parse(endDateStr));

            leaveRequestDAO.createLeaveRequest(leaveRequest);
            response.sendRedirect("leave_list");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
