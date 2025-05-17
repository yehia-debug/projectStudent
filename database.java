package projectt;

import java.sql.*;

public class database {
    private static final String port = ":3306";
    private static final String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    private static final String dbURL = "jdbc:mysql://localhost" + port + "/university";
    private static final String username = "root";
    private static final String password = "";
    
    private Connection conn;
    private Statement stmt;

    public database() throws SQLException, ClassNotFoundException {
        Class.forName(jdbcDriver);
        this.conn = DriverManager.getConnection(dbURL, username, password);
        this.stmt = conn.createStatement();
    }

    // Student operations
  public void addStudentWithMarks(String id, String name, String subject, double mark) throws SQLException {
    String sql = "INSERT INTO studentdb (id, name, subject, mark) VALUES (" +
                 "'" + id + "', " +
                 "'" + name + "', " +
                 "'" + subject + "', " +
                 mark + ")";
    stmt.executeUpdate(sql);
}
  public ResultSet getStudentMarks(String studentId) throws SQLException {
    // This should query the marks table specifically
    String sql = "SELECT subject, mark FROM studentdb WHERE id = ?";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setString(1, studentId);
    return stmt.executeQuery();
}

  public ResultSet searchStudent(String searchTerm) throws SQLException {
    // Search by either ID or name
    String sql = "SELECT DISTINCT id, name FROM studentdb WHERE id = ? OR name LIKE ?";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setString(1, searchTerm);
    stmt.setString(2, "%" + searchTerm + "%");
    return stmt.executeQuery();
}
   

public int deleteStudent(String identifier) throws SQLException {
    // Delete by either ID or name
    String sql = "DELETE FROM studentdb WHERE id = ? OR name = ?";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setString(1, identifier);
    stmt.setString(2, identifier);
    return stmt.executeUpdate(); // Returns number of rows deleted
}

    public ResultSet getAllMarks() throws SQLException {
    return stmt.executeQuery("SELECT id, name, subject, mark FROM studentdb ORDER BY id, subject");
}

    public boolean studentSubjectExists(String studentId, String subject) throws SQLException {
    String sql = "SELECT 1 FROM studentdb WHERE id = ? AND subject = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, studentId);
        stmt.setString(2, subject);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next();
        }
    }
}

public boolean updateStudentMark(String studentId, String subject, double newMark) throws SQLException {
    String sql = "UPDATE studentdb SET mark = ? WHERE id = ? AND subject = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setDouble(1, newMark);
        stmt.setString(2, studentId);
        stmt.setString(3, subject);
        return stmt.executeUpdate() > 0;
    }
}
    
    
    public void close() throws SQLException {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    }
}