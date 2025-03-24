package com.leave.servlet;

import com.leave.dao.LeaveRequestDAO;
import com.leave.dao.UserDAO;
import com.leave.model.LeaveRequest;
import com.leave.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
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

        // Lấy tham số tháng và năm từ request (nếu không có thì mặc định là tháng hiện tại)
        String monthParam = request.getParameter("month");
        String yearParam = request.getParameter("year");
        YearMonth yearMonth;
        if (monthParam != null && yearParam != null) {
            try {
                int month = Integer.parseInt(monthParam);
                int year = Integer.parseInt(yearParam);
                yearMonth = YearMonth.of(year, month);
            } catch (NumberFormatException e) {
                yearMonth = YearMonth.now(); // Mặc định là tháng hiện tại nếu tham số không hợp lệ
            }
        } else {
            yearMonth = YearMonth.now(); // Mặc định là tháng hiện tại
        }

        // Xác định ngày đầu và ngày cuối của tháng
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Lấy danh sách nhân viên và đơn xin nghỉ đã duyệt
        UserDAO userDAO = new UserDAO();
        LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
        List<User> employees = userDAO.getAllEmployees();
        List<LeaveRequest> approvedLeaves = leaveRequestDAO.getApprovedLeaves();

        // Tạo dữ liệu lịch
        List<Map<String, Object>> calendarData = new ArrayList<>();
        for (User employee : employees) {
            Map<String, Object> employeeData = new HashMap<>();
            employeeData.put("employee", employee.getUsername());

            // Tạo danh sách trạng thái cho từng ngày trong tháng
            List<String> dailyStatus = new ArrayList<>();
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                boolean isOnLeave = false;
                for (LeaveRequest leave : approvedLeaves) {
                    if (leave.getUserId() != employee.getId()) {
                        continue;
                    }
                    LocalDate leaveStart = ((java.sql.Date) leave.getStartDate()).toLocalDate();
                    LocalDate leaveEnd = ((java.sql.Date) leave.getEndDate()).toLocalDate();
                    if (!currentDate.isBefore(leaveStart) && !currentDate.isAfter(leaveEnd)) {
                        isOnLeave = true;
                        break;
                    }
                }
                dailyStatus.add(isOnLeave ? "Nghỉ" : "Đi làm");
                currentDate = currentDate.plusDays(1);
            }
            employeeData.put("dailyStatus", dailyStatus);
            calendarData.add(employeeData);
        }

        // Truyền dữ liệu vào JSP
        request.setAttribute("calendarData", calendarData);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("yearMonth", yearMonth);
        request.getRequestDispatcher("agenda.jsp").forward(request, response);
    }
}