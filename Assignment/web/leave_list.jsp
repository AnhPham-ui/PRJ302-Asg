<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.leave.model.LeaveRequest, com.leave.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>Danh sách đơn xin nghỉ</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <h2>Danh sách đơn xin nghỉ</h2>
        <% 
            User user = (User) session.getAttribute("user");
            if (user.getRoleId() == 1 || user.getRoleId() == 2) { // Employee hoặc Team Leader có thể tạo đơn
        %>
            <a href="leave_request" class="agenda-btn">Tạo đơn mới</a>
        <% } %>
        
        <!-- Hiển thị danh sách đơn của chính mình (cho Employee và Team Leader) -->
        <% if (user.getRoleId() == 1 || user.getRoleId() == 2) { %>
            <h3>Đơn xin nghỉ của bạn</h3>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Lý do</th>
                    <th>Ngày bắt đầu</th>
                    <th>Ngày kết thúc</th>
                    <th>Trạng thái</th>
                    <th>Người xử lý</th>
                    <th>Lý do xử lý</th>
                </tr>
                <% 
                    List<LeaveRequest> myLeaveRequests = (List<LeaveRequest>) request.getAttribute("myLeaveRequests");
                    if (myLeaveRequests != null) {
                        for (LeaveRequest leaveReq : myLeaveRequests) {
                %>
                    <tr>
                        <td><%= leaveReq.getId() %></td>
                        <td><%= leaveReq.getReason() %></td>
                        <td><%= leaveReq.getStartDate() %></td>
                        <td><%= leaveReq.getEndDate() %></td>
                        <td class="<%= leaveReq.getStatus().equals("Approved") ? "green" : leaveReq.getStatus().equals("Rejected") ? "red" : "" %>">
                            <%= leaveReq.getStatus() %>
                        </td>
                        <td><%= leaveReq.getProcessedBy() != null ? leaveReq.getProcessedBy() : "" %></td>
                        <td><%= leaveReq.getProcessReason() != null ? leaveReq.getProcessReason() : "" %></td>
                    </tr>
                <% 
                        }
                    }
                %>
            </table>
        <% } %>

        <!-- Hiển thị danh sách đơn cần xét duyệt (cho Team Leader và Head) -->
        <% if (user.getRoleId() == 2 || user.getRoleId() == 3) { %>
            <h3>Đơn xin nghỉ cần xét duyệt</h3>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Nhân viên</th>
                    <th>Lý do</th>
                    <th>Ngày bắt đầu</th>
                    <th>Ngày kết thúc</th>
                    <th>Trạng thái</th>
                    <th>Người xử lý</th>
                    <th>Lý do xử lý</th>
                    <th>Hành động</th>
                </tr>
                <% 
                    List<LeaveRequest> leaveRequests = (List<LeaveRequest>) request.getAttribute("leaveRequests");
                    if (leaveRequests != null) {
                        for (LeaveRequest leaveReq : leaveRequests) {
                            String employeeName = (String) request.getAttribute("employeeName_" + leaveReq.getUserId());
                            if (employeeName == null) {
                                employeeName = "User " + leaveReq.getUserId();
                            }
                %>
                    <tr>
                        <td><%= leaveReq.getId() %></td>
                        <td><%= employeeName %></td>
                        <td><%= leaveReq.getReason() %></td>
                        <td><%= leaveReq.getStartDate() %></td>
                        <td><%= leaveReq.getEndDate() %></td>
                        <td class="<%= leaveReq.getStatus().equals("Approved") ? "green" : leaveReq.getStatus().equals("Rejected") ? "red" : "" %>">
                            <%= leaveReq.getStatus() %>
                        </td>
                        <td><%= leaveReq.getProcessedBy() != null ? leaveReq.getProcessedBy() : "" %></td>
                        <td><%= leaveReq.getProcessReason() != null ? leaveReq.getProcessReason() : "" %></td>
                        <td>
                            <% if ("Inprogress".equals(leaveReq.getStatus())) { %>
                                <a href="approve_reject?id=<%= leaveReq.getId() %>" class="approve-btn">Xét duyệt</a>
                            <% } %>
                        </td>
                    </tr>
                <% 
                        }
                    }
                %>
            </table>
        <% } %>

        <% if (user.getRoleId() == 3) { %>
            <a href="agenda" class="agenda-btn">Xem Agenda</a>
        <% } %>
    </div>
</body>
</html>