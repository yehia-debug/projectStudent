package projectt;

import java.sql.*;

public class StudentManagementSystem {
    private final database db;
    private String adminPass;
    private Student[] students;
    private int studentCount;
    
    public StudentManagementSystem(database db) {
        this.db = db;
        this.adminPass = "admin123";
        this.students = new Student[100];
        this.studentCount = 0;
    }

    // Password verification
    public boolean verifyAdminPassword(String inputPass) {
        return adminPass.equals(inputPass);
    }

    public boolean changeAdminPassword(String oldPass, String newPass) {
        if (verifyAdminPassword(oldPass)) {
            adminPass = newPass;
            return true;
        }
        return false;
    }

    // Database operations with admin verification
    public boolean addStudentWithVerification(String id, String name, String subject, double mark, String adminPass) 
            throws SQLException {
        if (!verifyAdminPassword(adminPass)) return false;
        
        db.addStudentWithMarks(id, name, subject, mark);
        return true;
    }

    public int deleteStudentWithVerification(String identifier, String adminPass) 
            throws SQLException {
        if (!verifyAdminPassword(adminPass)) return 0;
        
        return db.deleteStudent(identifier);
    }
    public boolean modifyStudentMark(String studentId, String subject, double newMark, String adminPass) 
        throws SQLException {
    if (!verifyAdminPassword(adminPass)) {
        return false;
    }
    return db.updateStudentMark(studentId, subject, newMark);
}
    
}