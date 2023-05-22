import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;


/**
*   The ScoreBoard class is responsible for the programming of the scoreboard window.
*   It extends the JPanel class.
*/

public class ScoreBoard extends JPanel {
    private JTable scoreTable;
    private JTable resultsTable;

    /**
    *   Constructs a new instance of the ScoreBoard class.
    *   @param frame The main frame of the application.
    */

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

        // Create a JPanel with FlowLayout to center the label
        String[] scoreColumnNames = {"Rank", "Country", "Driver", "Points", "Wins", "Podiums"};
        scoreTable = createTable(scoreData, scoreColumnNames);
        JScrollPane scoreScrollPane = new JScrollPane(scoreTable);
        add(scoreScrollPane);

        // Create a JPanel with FlowLayout to center the label
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel resultsLabel = new JLabel("Ergebnisse");
        labelPanel.add(resultsLabel);
        add(labelPanel);

        // Updates the data in the scoreboard
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

     /**
     * Updates the data in the scoreboard.
     */
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

    
   /**
    * Loads the score data from the Score.csv file.
    * @return The loaded score data as a 2D array of strings.
    */
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
    
    /**
     * Loads the results data from the Results.csv file.
     * @return The loaded results data as a 2D array of strings.
    */
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

    /**
     * Creates a JTable with the given data and column names.
     * The created table is not editable.
     * @param data The data for the table as a 2D array of strings.
     * @param columnNames The column names for the table as an array of strings.
     * @return The created JTable.
    */
    private JTable createTable(String[][] data, String[] columnNames){
        return new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
    }

    /**
     * Reads the header line from the specified file and returns it as a string.
     * @param filePath The path to the file from which to read the header line.
     * @return The header line as a string, or an empty string if the file cannot be read.
    */
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



