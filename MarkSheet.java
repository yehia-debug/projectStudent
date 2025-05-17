package projectt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MarkSheet {
    private final database db;

    public MarkSheet(database db) {
        this.db = db;
    }

    public JPanel createMarksheetPanel() throws SQLException {
        // Create table model
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Name", "Subject", "Mark"}, 0);

        // Fetch data from database
        ResultSet rs = db.getAllMarks();
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("subject"),
                rs.getDouble("mark")
            });
        }
        rs.close();

        // Create table
        JTable table = new JTable(model);
       
        table.setRowHeight(25);

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        // Create panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Student Marksheet", JLabel.CENTER), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return panel;
    }
}