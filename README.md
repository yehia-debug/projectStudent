# Student Management System  

## 📌 Overview  
A **Java Swing** application for managing student records, marks, and grades. It connects to a **MySQL** database hosted locally via **XAMPP**, providing a secure and user-friendly interface for administrators.  

---

## ✨ Features  
✅ **Student Management**  
- Add, search, modify, and delete student records  
- Prevent duplicate subject entries for the same student  
- Calculate and display final grades  

🔒 **Admin Security**  
- Password-protected admin functions (`admin123` by default)  
- Change admin password securely  

📊 **Database Integration**  
- **MySQL** database via **XAMPP** (local server)  
- Stores student IDs, names, subjects, and marks  

---

## ⚙️ Setup Instructions  

### **1. XAMPP & MySQL Setup**  
1. **Install XAMPP**  
   - Download from [Apache Friends](https://www.apachefriends.org/)  
   - Start **Apache** and **MySQL** in the XAMPP Control Panel  


### **2. Configure Database Connection**  
- Open `database.java` and ensure these settings match your XAMPP MySQL:  
  ```java
  private static final String dbURL = "jdbc:mysql://localhost:3306/university";
  private static final String username = "root";  // Default XAMPP MySQL username
  private static final String password = "";      // Default XAMPP MySQL password (empty)
  ```

### **3. Run the Application**  
Compile and execute from the command line:  
```bash
javac projectt/*.java
java projectt.Projectt
```
*(Or use an IDE like **IntelliJ** or **Eclipse**)*  

---

## 🖥️ Usage Guide  
### **Main Menu Options**  
| Option | Description | Requires Admin Password? |
|--------|-------------|--------------------------|
| **Add Student** | Add new student with marks | ✔️ Yes |
| **Search Student** | Find students by ID or name | ✔️ Yes |
| **Delete Student** | Remove a student record | ✔️ Yes |
| **Modify Student** | Update a student's mark | ✔️ Yes |
| **View Marksheet** | Display all student records | ✔️ Yes |
| **Change Admin Password** | Update admin login | ✔️ Yes |
| **Exit** | Close the program | ❌ No |

### **Default Admin Credentials**  
- **Username:** *None (password-only access)*  
- **Password:** `admin123`  

---

## 🛠️ Troubleshooting  
🔹 **"Database connection failed"**  
- Ensure **XAMPP MySQL** is running.
- Ensure the table is named ' studentdb' in a 'university'
- Verify `username` and `password` in `database.java`.  

🔹 **Duplicate entry error**  
- The system prevents adding the same subject twice for one student.  

---

## 📂 Project Structure  
```
📁 projectt/
├── 📄 Projectt.java         (Main GUI & logic)
├── 📄 database.java         (MySQL connection & queries)
├── 📄 Student.java          (Student data model)
├── 📄 StudentManagementSystem.java (Admin functions)
└── 📄 MarkSheet.java        (Marksheet display)
```

---

## 📜 License  
**MIT License** – Free to use, modify, and distribute.  

---
