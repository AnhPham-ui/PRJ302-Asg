<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.YearMonth" %>
<!DOCTYPE html>
<html>
<head>
    <title>Agenda - Leave Management</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
        .on-leave {
            background-color: #ffcccc;
        }
        .working {
            background-color: #ccffcc;
        }
    </style>
</head>
<body>
    <h2>Agenda - Leave Calendar</h2>

    <!-- Form để chọn tháng và năm -->
    <form method="get" action="agenda">
        <label for="month">Month:</label>
        <select name="month" id="month">
            <c:forEach var="i" begin="1" end="12">
                <option value="${i}" ${i == yearMonth.monthValue ? 'selected' : ''}>${i}</option>
            </c:forEach>
        </select>
        <label for="year">Year:</label>
        <select name="year" id="year">
            <c:forEach var="i" begin="2020" end="2030">
                <option value="${i}" ${i == yearMonth.year ? 'selected' : ''}>${i}</option>
            </c:forEach>
        </select>
        <button type="submit">View Calendar</button>
    </form>

    <h3>Calendar for ${yearMonth.month} ${yearMonth.year}</h3>

    <!-- Bảng lịch -->
    <table>
        <thead>
            <tr>
                <th>Employee</th>
                <c:set var="currentDate" value="${startDate}"/>
                <c:forEach begin="0" end="${endDate.dayOfMonth - 1}">
                    <th>${currentDate.dayOfMonth}</th>
                    <c:set var="currentDate" value="${currentDate.plusDays(1)}"/>
                </c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="employeeData" items="${calendarData}">
                <tr>
                    <td>${employeeData.employee}</td>
                    <c:forEach var="status" items="${employeeData.dailyStatus}">
                        <td class="${status == 'Nghỉ' ? 'on-leave' : 'working'}">${status}</td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>