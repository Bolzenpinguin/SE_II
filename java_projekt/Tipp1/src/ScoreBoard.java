import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ScoreBoard extends JPanel {

    public ScoreBoard(MainFrame frame) {
        setLayout(new BorderLayout());

        // get Data from CSV
        String[][] data = loadData();

        // get first line as heading
        JLabel headingLabel = new JLabel(data[0][0], SwingConstants.CENTER);
        add(headingLabel, BorderLayout.NORTH);

        String[] columnNames = {"Rank", "Country", "Driver", "Points", "Wins", "Podiums"};
        JTable table = new JTable(Arrays.copyOfRange(data, 1, data.length), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // This causes all cells to be not editable
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));
        add(backButton, BorderLayout.PAGE_END);
    }

    private String[][] loadData() {
        // read csv file and get all the content -> "," is the seperator
        ArrayList<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/textContent/Score.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                data.add(rowData);
            }
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }
        return data.toArray(new String[0][0]);
    }
}
