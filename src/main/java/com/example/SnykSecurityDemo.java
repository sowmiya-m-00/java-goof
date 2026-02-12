package com.example;

import java.io.*;
import java.sql.*;
import javax.servlet.http.*;

public class SnykSecurityDemo extends HttpServlet {

    // 1. Hardcoded Credentials (Snyk Code High Severity)
    private static final String DB_PASSWORD = "admin_password_12345";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String input = request.getParameter("username");

        // 2. SQL Injection (Snyk Code Critical Severity)
        // Untrusted input is concatenated directly into the query string
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db", "root", DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE username = '" + input + "'";
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 3. Reflected Cross-Site Scripting (XSS) (Snyk Code High Severity)
        // Input is written directly back to the response without sanitization
        response.getWriter().println("<h1>Welcome, " + input + "</h1>");

        // 4. Path Traversal (Snyk Code High Severity)
        // User input determines which file is read from the disk
        String fileName = request.getParameter("file");
        File file = new File("/var/data/" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        response.getWriter().println(reader.readLine());
    }
}
