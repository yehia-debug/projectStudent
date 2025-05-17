
package projectt;
import java.sql.*;

public class Student  {
    private String studentId;
    private String name;
    private String[] subjects;
    private double[] marks;
    private String grade;
    private int markCount;
    
    
    public Student(String studentId,String name, int maxSubjects){
        this.studentId = studentId;
        this.name=name;
        this.subjects=new String[maxSubjects];
        this.marks=new double[maxSubjects];
        this.grade="not calculated";
        this.markCount=0;
       
    }
    public void addMark(String subject,double mark){
        
        if(markCount<subjects.length){
            subjects[markCount]=subject;
            marks[markCount]=mark;
            markCount++;
            calculateGrade();
        }else{
            System.out.println("no more subkectes");
        }
    }
    
    public void calculateGrade() {
        if (markCount == 0) {
            grade = "no marks";
            return;
        }
        
        double total = 0;
        for (int i =0;i<markCount;i++) {
            total += marks[i];
        }
        double average = total/markCount;
        
        if (average >= 90) grade = "A";
        else if (average >= 80) grade = "B";
        else if (average >= 70) grade = "C";
        else if (average >= 60) grade = "D";
        else grade = "F";
    }
public void loadMarksFromDatabase(database db) throws SQLException {
    ResultSet rs = db.getStudentMarks(this.studentId); 
    while (rs.next()) {
        String subject = rs.getString("subject");
        double mark = rs.getDouble("mark"); 
        this.addMark(subject, mark);
    }
    rs.close();
}
    
    public String getStudentId(){return studentId;}
    public String getName(){return name;}
    public String getGrade(){return grade;}
    public int getMarkCount(){return markCount;}
    public String getSubject(int index){return subjects[index];}    
    public double getMark(int index){return marks[index];}        
         

    
}
