package com.leave.servlet;

import com.leave.dao.LeaveRequestDAO;
import com.leave.dao.UserDAO;
import com.leave.model.LeaveRequest;
import com.leave.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleId() != 3) { // Chỉ Head (roleId = 3) được xem agenda
            response.sendRedirect("login");
            return;
        }

        // Lấy danh sách nhân viên và đơn xin nghỉ đã duyệt
        UserDAO userDAO = new UserDAO();
        LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
        List<User> employees = userDAO.getAllEmployees();
        List<LeaveRequest> approvedLeaves = leaveRequestDAO.getApprovedLeaves();

        // Tạo dữ liệu agenda
        List<Map<String, Object>> agendaData = new ArrayList<>();
        LocalDate startDate = LocalDate.now().withDayOfMonth(1); // Bắt đầu từ đầu tháng
        LocalDate endDate = LocalDate.now(); // Đến ngày hiện tại
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (User employee : employees) {
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("employee", employee.getUsername());
                entry.put("date", currentDate.format(formatter));

                // Kiểm tra xem nhân viên có nghỉ ngày này không
                boolean isOnLeave = false;
                for (LeaveRequest leave : approvedLeaves) {
                    if (leave.getUserId() != employee.getId()) {
                        continue; // Bỏ qua nếu đơn xin nghỉ không phải của nhân viên này
                    }
                    // Chuyển java.sql.Date thành LocalDate
                    LocalDate leaveStart = ((java.sql.Date) leave.getStartDate()).toLocalDate();
                    LocalDate leaveEnd = ((java.sql.Date) leave.getEndDate()).toLocalDate();
                    if (!currentDate.isBefore(leaveStart) && !currentDate.isAfter(leaveEnd)) {
                        isOnLeave = true;
                        break;
                    }
                }
                entry.put("status", isOnLeave ? "Nghỉ" : "Đi làm");
                agendaData.add(entry);
                currentDate = currentDate.plusDays(1);
            }
        }

        // Truyền dữ liệu vào JSP
        request.setAttribute("agendaData", agendaData);
        request.getRequestDispatcher("agenda.jsp").forward(request, response);
    }
}