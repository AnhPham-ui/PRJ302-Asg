package com.leave.servlet;

import com.leave.dao.LeaveRequestDAO;
import com.leave.dao.UserDAO;
import com.leave.model.LeaveRequest;
import com.leave.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
        UserDAO userDAO = new UserDAO();
        List<LeaveRequest> leaveRequests = null;
        List<LeaveRequest> myLeaveRequests = null;

        // Lấy danh sách đơn của chính người dùng (cho Employee và Team Leader)
        if (user.getRoleId() == 1 || user.getRoleId() == 2) {
            myLeaveRequests = leaveRequestDAO.getLeaveRequestsByUser(user.getId());
        }

        // Lấy danh sách đơn cần xét duyệt (cho Team Leader và Head)
        if (user.getRoleId() == 2 || user.getRoleId() == 3) {
            leaveRequests = leaveRequestDAO.getLeaveRequestsForManager(user.getId());
            // Lấy thông tin username của nhân viên
            Map<Integer, String> employeeNames = new HashMap<>();
            for (LeaveRequest leaveReq : leaveRequests) {
                User employee = userDAO.getUserById(leaveReq.getUserId());
                if (employee != null) {
                    employeeNames.put(leaveReq.getUserId(), employee.getUsername());
                } else {
                    employeeNames.put(leaveReq.getUserId(), "User " + leaveReq.getUserId());
                }
            }
            for (Map.Entry<Integer, String> entry : employeeNames.entrySet()) {
                request.setAttribute("employeeName_" + entry.getKey(), entry.getValue());
            }
        } else if (user.getRoleId() == 1) {
            leaveRequests = leaveRequestDAO.getLeaveRequestsByUser(user.getId());
        } else {
            response.sendRedirect("login");
            return;
        }

        request.setAttribute("myLeaveRequests", myLeaveRequests);
        request.setAttribute("leaveRequests", leaveRequests);
        request.getRequestDispatcher("leave_list.jsp").forward(request, response);
    }
}