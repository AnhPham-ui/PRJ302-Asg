<%-- 
    Document   : approve_reject
    Created on : Mar 10, 2025, 9:04:11 AM
    Author     : DELL
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Xét duyệt đơn xin nghỉ</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <h2>Xét duyệt đơn xin nghỉ</h2>
        <form action="approve_reject" method="post">
            <input type="hidden" name="requestId" value="<%= request.getAttribute("requestId") %>">
            <label>Trạng thái:</label>
            <select name="status" required>
                <option value="Approved">Approve</option>
                <option value="Rejected">Reject</option>
            </select>
            <label>Lý do xử lý:</label>
            <textarea name="processReason" rows="5" cols="30" required></textarea>
            <input type="submit" value="Xác nhận">
        </form>
        <a href="leave_list" class="agenda-btn">Quay lại danh sách</a>
    </div>
</body>
</html>