<%-- 
    Document   : leave_request
    Created on : Mar 10, 2025, 9:03:24 AM
    Author     : DELL
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Tạo đơn xin nghỉ</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <h2>Tạo đơn xin nghỉ</h2>
        <form action="leave_request" method="post">
            <label>Lý do xin nghỉ:</label>
            <textarea name="reason" rows="5" cols="30" required></textarea>
            <label>Ngày bắt đầu:</label>
            <input type="date" name="startDate" required>
            <label>Ngày kết thúc:</label>
            <input type="date" name="endDate" required>
            <input type="submit" value="Gửi đơn">
        </form>
        <a href="leave_list" class="agenda-btn">Quay lại danh sách</a>
    </div>
</body>
</html>