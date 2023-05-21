import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ScoreBoard extends JPanel {
    private JTable scoreTable;
    private JTable resultsTable;

    public ScoreBoard(MainFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // get Data from CSV
        String[][] scoreData = loadScoreData();
        String[][] resultsData = loadResultsData();

        // get first line as heading
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headingLabel = new JLabel(getHeaderLine("java_projekt/Tipp1/textContent/Score.csv"), SwingConstants.CENTER);
        headerPanel.add(headingLabel);
        add(headerPanel);

        String[] scoreColumnNames = {"Rank", "Country", "Driver", "Points", "Wins", "Podiums"};
        scoreTable = createTable(scoreData, scoreColumnNames);
        JScrollPane scoreScrollPane = new JScrollPane(scoreTable);
        add(scoreScrollPane);

        // Create a JPanel with FlowLayout to center the label
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel resultsLabel = new JLabel("Ergebnisse");
        labelPanel.add(resultsLabel);
        add(labelPanel);

        String[] resultColumnNames = {"Driver", "Points"};
        resultsTable = createTable(resultsData, resultColumnNames);
        JScrollPane resultsScrollPane = new JScrollPane(resultsTable);
        add(resultsScrollPane);

        // Create a JPanel with FlowLayout to center the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));
        buttonPanel.add(backButton);
        add(buttonPanel);
    }

    public void updateData() {
        String[][] scoreData = loadScoreData();
        String[][] resultsData = loadResultsData();

        scoreTable.setModel(new DefaultTableModel(
            scoreData,
            new String[]{"Platzierung", "Land", "Fahrer", "Punkte", "Gewinne", "Podeste"}));

        resultsTable.setModel(new DefaultTableModel(
            resultsData,
            new String[]{"Fahrer", "Erreichte Punktzahl"}));
    }

    private String[][] loadScoreData() {
        ArrayList<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/textContent/Score.csv"))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                data.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toArray(new String[0][]);
    }
    
    private String[][] loadResultsData() {
        ArrayList<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/textContent/Results.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                data.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toArray(new String[0][]);
    }

    private JTable createTable(String[][] data, String[] columnNames){
        return new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
    }

    private String getHeaderLine(String filePath){
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            if ((line = br.readLine()) != null) {}
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}
