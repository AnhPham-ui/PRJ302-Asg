<%-- 
    Document   : agenda
    Created on : Mar 23, 2025
    Author     : Grok 3 (xAI)
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <title>Agenda</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <h2>Agenda - Lịch làm việc</h2>
        <table>
            <tr>
                <th>Nhân viên</th>
                <th>Ngày</th>
                <th>Trạng thái</th>
            </tr>
            <% 
                List<Map<String, Object>> agendaData = (List<Map<String, Object>>) request.getAttribute("agendaData");
                if (agendaData != null && !agendaData.isEmpty()) {
                    for (Map<String, Object> entry : agendaData) {
                        String status = (String) entry.get("status");
                        String statusClass = status.equals("Nghỉ") ? "red" : "green";
            %>
                <tr>
                    <td><%= entry.get("employee") %></td>
                    <td><%= entry.get("date") %></td>
                    <td class="<%= statusClass %>"><%= status %></td>
                </tr>
            <% 
                    }
                } else {
            %>
                <tr>
                    <td colspan="3">Không có dữ liệu lịch làm việc.</td>
                </tr>
            <% 
                }
            %>
        </table>
        <a href="leave_list" class="agenda-btn">Quay lại danh sách</a>
    </div>
</body>
</html>