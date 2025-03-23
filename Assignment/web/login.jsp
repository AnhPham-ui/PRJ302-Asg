<%-- 
    Document   : login
    Created on : Mar 10, 2025, 9:01:37 AM
    Author     : DELL
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="login-container">
        <h2>Đăng Nhập</h2>
        <form action="login" method="post">
            <label>Tên đăng nhập:</label>
            <input type="text" name="username" required>
            <label>Mật khẩu:</label>
            <input type="password" name="password" required>
            <input type="submit" value="Đăng Nhập">
        </form>
        <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>
    </div>
</body>
</html>

