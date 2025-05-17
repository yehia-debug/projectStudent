
package projectt;




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

public class Projectt {
     static StudentManagementSystem system;
     static database db ;
     static{
         try{
             db=new database();
             system= new StudentManagementSystem(db);
             
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, 
                "Database connection failed: " + e.getMessage(),
                "Startup Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
             
         }
     }
    
    
    
    public static void main(String[] args) {
   
                JFrame frame = new JFrame("Student Management System");
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 500);
                
                JPanel panel = new JPanel (new BorderLayout());
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                
                JLabel title=new JLabel("Student managemtn system");
                
                addButton(panel,"add student", e -> addStudent());
                addButton(panel, "Search student", e ->searchStudent ());
                addButton(panel, "delete student", e ->deleteStudent() );
                addButton(panel,"modify student", e -> modifyStudent());
                addButton(panel,"View marksheet",e ->viewMarksheet()  );
                addButton(panel, "Exit", e -> System.exit(0));
                addButton(panel,"change adminPassword",e -> changeAdminPassword() );
        
               
                
        
               frame.add(panel);
                 frame.setLocationRelativeTo(null); 
                frame.setVisible(true);
}


     private static void addButton(JPanel panel, String name, ActionListener action) {
        // Create space between buttons (except before first button)
        if (panel.getComponentCount() > 0) {
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
     JButton button=new JButton(name);
     button.setMaximumSize(new Dimension(250, 50));
        button.setPreferredSize(new Dimension(250, 50));
    
     
     
     button.addActionListener(action);
        
        panel.add(button);
     }
     
   private static void addStudent() {
    // Password verification loop
    while (true) {
        String password = JOptionPane.showInputDialog("Enter admin password:");
        
        // User clicked cancel or closed the dialog
        if (password == null) {
            return;
        }
        
        // Check password
        if (!system.verifyAdminPassword(password)) {
            JOptionPane.showMessageDialog(
                null,
                "Wrong password! Try again.",
                "Authentication Failed",
                JOptionPane.ERROR_MESSAGE
            );
            continue;
        }
        
        // Password correct - proceed with student addition
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField subjectField = new JTextField();
        JTextField markField = new JTextField();
        
        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Subject:"));
        formPanel.add(subjectField);
        formPanel.add(new JLabel("Mark (0-100):"));
        formPanel.add(markField);

        int result = JOptionPane.showConfirmDialog(
            null,
            formPanel,
            "Add New Student",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String subject = subjectField.getText().trim();
                double mark = Double.parseDouble(markField.getText().trim());

                if (id.isEmpty() || name.isEmpty() || subject.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required");
                }
                if (mark < 0 || mark > 100) {
                    throw new IllegalArgumentException("Mark must be 0-100");
                }

                // Check if student-subject combination already exists
                if (db.studentSubjectExists(id, subject)) {
                    JOptionPane.showMessageDialog(
                        null,
                        "This student already has a mark for " + subject + "!\n" +
                        "Please use 'Modify Student' to update the mark.",
                        "Duplicate Entry",
                        JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }

                // Add to database
                db.addStudentWithMarks(id, name, subject, mark);

                JOptionPane.showMessageDialog(
                    null,
                    "Student added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                break;

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Invalid mark format",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Database error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            break;
        }
    }
}


     
  private static void searchStudent() {
    String searchTerm = JOptionPane.showInputDialog("Enter student ID or name:");
    
    if (searchTerm == null || searchTerm.trim().isEmpty()) return;
    
    try {
        // 1. First find student ID
        ResultSet studentInfo = db.searchStudent(searchTerm);
        if (!studentInfo.next()) {
            JOptionPane.showMessageDialog(null, "Student not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String id = studentInfo.getString("id");
        String name = studentInfo.getString("name");
        studentInfo.close();
        
        // 2. Now load marks using the correct method
        Student student = new Student(id, name, 20);
        student.loadMarksFromDatabase(db);
        
        // 3. Build and display results
        String message = "ID: " + id + "\nName: " + name + "\n\nSubjects:\n";
        
        for (int i = 0; i < student.getMarkCount(); i++) {
            message += "- " + student.getSubject(i) + ": " + student.getMark(i) + "\n";
        }
        
        message += "\nFinal Grade: " + student.getGrade();
        
        JOptionPane.showMessageDialog(null, message, "Student Record", JOptionPane.INFORMATION_MESSAGE);
        
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, 
            "Database error: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        
    }
}
  
     private static void deleteStudent() {
    // Password verification loop
    while (true) {
        String password = JOptionPane.showInputDialog("Enter admin password:");
        
        // User clicked cancel or closed the dialog
        if (password == null) {
            return;
        }
        
        // Check password
        if (!system.verifyAdminPassword(password)) {
            JOptionPane.showMessageDialog(
                null,
                "Wrong password! Try again.",
                "Authentication Failed",
                JOptionPane.ERROR_MESSAGE
            );
            continue; // Keep asking for password
        }
        
        // Password correct - proceed with deletion
        String searchTerm = JOptionPane.showInputDialog("Enter student ID or name to delete:");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return;
        }

        try {
            int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this student and all their marks?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int rowsDeleted = db.deleteStudent(searchTerm);
                
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Successfully deleted " + rowsDeleted + " records",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "No student found with that ID/name",
                        "Not Found",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
                break; // Exit password loop after operation
            } else {
                break; // User cancelled deletion
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                null,
                "Database error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            break;
        }
    }
}
    
     
 private static void modifyStudent() {
    // Password verification loop
    while (true) {
        String password = JOptionPane.showInputDialog(null, "Enter admin password:", "Admin Verification", JOptionPane.PLAIN_MESSAGE);
        if (password == null) return; // User cancelled
        
        if (!system.verifyAdminPassword(password)) {
            JOptionPane.showMessageDialog(null, "Wrong password! Try again.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            continue;
        }

        try {
            // Get student ID/name
            String searchTerm;
            while (true) {
                searchTerm = JOptionPane.showInputDialog(null, "Enter student ID or name:", "Input Required", JOptionPane.QUESTION_MESSAGE);
                if (searchTerm == null) return; // User cancelled
                if (!searchTerm.trim().isEmpty()) break;
                JOptionPane.showMessageDialog(null, "Student ID/name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Search for student
            ResultSet studentInfo = db.searchStudent(searchTerm);
            if (!studentInfo.next()) {
                JOptionPane.showMessageDialog(null, "Student not found", "Error", JOptionPane.ERROR_MESSAGE);
                studentInfo.close();
                continue;
            }

            String id = studentInfo.getString("id");
            String name = studentInfo.getString("name");
            studentInfo.close();

            // Get subject
            String subject;
            while (true) {
                subject = JOptionPane.showInputDialog(null, "Enter subject to modify:", "Input Required", JOptionPane.QUESTION_MESSAGE);
                if (subject == null) return; // User cancelled
                if (!subject.trim().isEmpty()) break;
                JOptionPane.showMessageDialog(null, "Subject cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Get new mark
            Double newMark = null;
            while (newMark == null) {
                String markStr = JOptionPane.showInputDialog(null, "Enter new mark (0-100):", "Input Required", JOptionPane.QUESTION_MESSAGE);
                if (markStr == null) return; // User cancelled
                
                try {
                    double mark = Double.parseDouble(markStr);
                    if (mark >= 0 && mark <= 100) {
                        newMark = mark;
                    } else {
                        JOptionPane.showMessageDialog(null, "Mark must be between 0 and 100", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Execute modification
            boolean success = system.modifyStudentMark(id, subject, newMark, password);
            
            if (success) {
                JOptionPane.showMessageDialog(null, "Mark updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Student or subject not found", "Not Found", JOptionPane.ERROR_MESSAGE);
            }
            break;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            break;
        }
    }
}  
  private static void viewMarksheet() {
    try {
        MarkSheet markSheet = new MarkSheet(db);
        JPanel marksheetPanel = markSheet.createMarksheetPanel();
        
        JFrame frame = new JFrame("Student Marksheet");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(marksheetPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(
            null,
            "Database error: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
  
private static void changeAdminPassword() {
    while (true) {
        // Get current password
        String currentPass = JOptionPane.showInputDialog(
            null, 
            "Enter current admin password:", 
            "Password Verification", 
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (currentPass == null) return;
       
                if (!system.verifyAdminPassword(currentPass)) {
            JOptionPane.showMessageDialog(
                null,
                "Incorrect password! Please try again.",
                "Authentication Failed",
                JOptionPane.ERROR_MESSAGE
            );
            continue; // Go back to password entry
        }

        
        // Get new password
        String newPass = JOptionPane.showInputDialog(
            null, 
            "Enter new admin password:", 
            "New Password", 
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (newPass == null) return; // User cancelled
        
        // Confirm new password
        String confirmPass = JOptionPane.showInputDialog(
            null, 
            "Confirm new admin password:", 
            "Confirm Password", 
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (confirmPass == null) return; // User cancelled
        
        // Validate inputs
        if (newPass.isEmpty()) {
            JOptionPane.showMessageDialog(
                null, 
                "New password cannot be empty", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            continue;
        }
        
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(
                null, 
                "New passwords do not match", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            continue;
        }
        
        // Try to change password
        try {
            boolean success = system.changeAdminPassword(currentPass, newPass);
            
            if (success) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Password changed successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE
                );
                break;
            } else {
                JOptionPane.showMessageDialog(
                    null, 
                    "Current password is incorrect", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            break;
        }
    }
}
      
     
     
     
     
     
     
}
