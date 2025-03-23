package com.leave.dao;

import com.leave.model.LeaveRequest;
import com.leave.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestDAO {
    public void createLeaveRequest(LeaveRequest request) {
        String sql = "INSERT INTO leave_requests (user_id, reason, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, request.getUserId());
            stmt.setString(2, request.getReason());
            stmt.setDate(3, new java.sql.Date(request.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(request.getEndDate().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LeaveRequest> getLeaveRequestsByUser(int userId) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM leave_requests WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LeaveRequest request = new LeaveRequest();
                request.setId(rs.getInt("id"));
                request.setUserId(rs.getInt("user_id"));
                request.setReason(rs.getString("reason"));
                request.setStartDate(rs.getDate("start_date"));
                request.setEndDate(rs.getDate("end_date"));
                request.setStatus(rs.getString("status"));
                request.setProcessedBy(rs.getObject("processed_by") != null ? rs.getInt("processed_by") : null);
                request.setProcessReason(rs.getString("process_reason"));
                request.setCreatedAt(rs.getTimestamp("created_at"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<LeaveRequest> getLeaveRequestsForManager(int managerId) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql;
        // Kiểm tra vai trò của người dùng
        UserDAO userDAO = new UserDAO();
        User manager = userDAO.getUserById(managerId);
        if (manager != null && manager.getRoleId() == 3) {
            // Nếu là Head (roleId = 3), lấy tất cả đơn của Team Leader (roleId = 2)
            sql = "SELECT lr.* FROM leave_requests lr JOIN users u ON lr.user_id = u.id WHERE u.role_id = 2";
        } else {
            // Nếu là Team Leader (roleId = 2), lấy đơn của nhân viên trực tiếp dưới quyền
            sql = "SELECT lr.* FROM leave_requests lr JOIN users u ON lr.user_id = u.id WHERE u.manager_id = ?";
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (manager == null || manager.getRoleId() != 3) {
                stmt.setInt(1, managerId);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LeaveRequest request = new LeaveRequest();
                request.setId(rs.getInt("id"));
                request.setUserId(rs.getInt("user_id"));
                request.setReason(rs.getString("reason"));
                request.setStartDate(rs.getDate("start_date"));
                request.setEndDate(rs.getDate("end_date"));
                request.setStatus(rs.getString("status"));
                request.setProcessedBy(rs.getObject("processed_by") != null ? rs.getInt("processed_by") : null);
                request.setProcessReason(rs.getString("process_reason"));
                request.setCreatedAt(rs.getTimestamp("created_at"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public void updateLeaveRequestStatus(int requestId, String status, int processedBy, String processReason) {
        String sql = "UPDATE leave_requests SET status = ?, processed_by = ?, process_reason = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, processedBy);
            stmt.setString(3, processReason);
            stmt.setInt(4, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LeaveRequest> getApprovedLeaves() {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM leave_requests WHERE status = 'Approved'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LeaveRequest request = new LeaveRequest();
                request.setId(rs.getInt("id"));
                request.setUserId(rs.getInt("user_id"));
                request.setReason(rs.getString("reason"));
                request.setStartDate(rs.getDate("start_date"));
                request.setEndDate(rs.getDate("end_date"));
                request.setStatus(rs.getString("status"));
                request.setProcessedBy(rs.getObject("processed_by") != null ? rs.getInt("processed_by") : null);
                request.setProcessReason(rs.getString("process_reason"));
                request.setCreatedAt(rs.getTimestamp("created_at"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
}