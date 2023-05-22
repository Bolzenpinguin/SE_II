import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class is responsible for programming the game window.
 * It extends the JPanel class and creates and manages the application's main menu.
 */

public class Play extends JPanel {
    private DefaultListModel<String> listModel;
    private JList<String> driverList;
    private ScoreBoard scoreBoard;

    /**
     * Constructor of the Play class.
     * Creates a game window with the given data.
     *
     * @param frame      The main window of the application.
     * @param scoreBoard The Scoreboard object for updating data.
     */

    public Play(MainFrame frame, ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        loadDriversFromCsv();

        driverList = new JList<>(listModel);
        driverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        driverList.setDragEnabled(true);
        driverList.setDropMode(DropMode.INSERT);
        driverList.setTransferHandler(new ListItemTransferHandler());

        // Set list renderer for rankings and center alignment
        driverList.setCellRenderer(new ListCellRenderer<>() {
            private JLabel label;

            {
                label = new JLabel();
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }

            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                label.setText((index + 1) + ". " + value);
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(driverList);
        scrollPane.setPreferredSize(new Dimension(200, 500)); // Set preferred size

        JPanel listPanel = new JPanel();
        listPanel.add(scrollPane);
        add(listPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        JButton evaluationButton = new JButton("Tipp auswerten");
        evaluationButton.addActionListener(e -> {
            saveOrder();
            updateScore();
            evaluateTips();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        buttonPanel.add(evaluationButton);

        add(buttonPanel, BorderLayout.PAGE_END);

    }

    /**
     * Loads the driver data from the score CSV file.
     * Adds the driver names to the list.
     */

    private void loadDriversFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/textContent/Score.csv"))) {
            String line;
            reader.readLine(); // Skip the header line
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 3) {  // Ensure we have enough fields
                    listModel.addElement(fields[2]);  // Add driver name
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TransferHandler class for list item drag-and-drop.
     */

    class ListItemTransferHandler extends TransferHandler {
        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new StringSelection(driverList.getSelectedValue());
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            if (action == MOVE) {
                listModel.remove(driverList.getSelectedIndex());
            }
        }

        @Override
        public boolean canImport(TransferSupport support) {
            return true;
        }

        @Override
        public boolean importData(TransferSupport support) {
            try {
                String data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
                listModel.add(dl.getIndex(), data);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
    * Saves the order of the drivers in a CSV file.
    */

    private void saveOrder() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("java_projekt/Tipp1/textContent/Tipp.csv"))) {
            for (int i = 0; i < listModel.getSize(); i++) {

                // Write the driver
                writer.write((i + 1) + "," + listModel.getElementAt(i));
                writer.newLine();
            }
            writer.flush(); // Ensure data is immediately written to the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
    * Evaluates the tips based on the results and saves the points in a CSV file.
    */

    private void evaluateTips() {
        String tippsFile = "java_projekt/Tipp1/textContent/Tipp.csv";
        String winnerFile = "java_projekt/Tipp1/textContent/Score.csv";
        String resultsFile = "java_projekt/Tipp1/textContent/Results.csv";

        try (BufferedReader tippsReader = new BufferedReader(new FileReader(tippsFile));
            BufferedReader resultReader = new BufferedReader(new FileReader(winnerFile));
            BufferedWriter resultsWriter = new BufferedWriter(new FileWriter(resultsFile))) {

        resultReader.readLine(); 

        String tippLine;
        String resultLine;

        while ((tippLine = tippsReader.readLine()) != null) {
            String[] tippData = tippLine.split(",");

            int platzierung = Integer.parseInt(tippData[0]);
            String tippFahrer = tippData[1];

            resultReader.mark(4096); 

            while ((resultLine = resultReader.readLine()) != null) {
                String[] resultData = resultLine.split(",");
                String resultFahrer = resultData[2]; // Index 2 für den Fahrernamen

                if (tippFahrer.equals(resultFahrer)) {
                    int resultPlacement = Integer.parseInt(resultData[0]);

                    int pointsGiven = calculatePoints(platzierung, resultPlacement);
                    
                    resultsWriter.write(tippFahrer + "," + pointsGiven);
                    resultsWriter.newLine();

                    break; // Fahrer gefunden, Schleife verlassen
                }
            }

            resultReader.reset(); // Setze den Stream auf die markierte Position zurück
        }
        resultsWriter.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    /** 
    * Calculates the points based on the tip placement and the result placement.
    *
    * @param tipPlacement     The placement of the tip.
    * @param resultPlacement  The actual result placement.
    * @return The points assigned to the tip.
    */
private static int calculatePoints(int tippPlacement, int resultPlacement) {
    int placeDiff = Math.abs(tippPlacement - resultPlacement);
    switch (placeDiff) {
        case 0:
            return 100;
        case 1:
            return 80;
        case 2:
            return 60;
        case 3:
            return 40;
        case 4:
            return 20;
        default:
            return 0; // for placeDiff > 4
    }
}

    /**
     * Updates the scores of the drivers randomly and saves them in a CSV file.
    */

    private void updateScore() {
        Random random = new Random();
        List<String[]> drivers = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/textContent/Score.csv"))) {
            String line;
            reader.readLine(); // skip the first line
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 6) {
                    int points = random.nextInt(101); // Generate random points from 0 to 100
                    int wins = random.nextInt(points / 25 + 1); // Wins should be less than or equal to points/25
                    int podiums = wins + random.nextInt((points - 25 * wins) / 10 + 1); // Podiums should be more than wins but less than or equal to points/10
                    drivers.add(new String[] {fields[1], fields[2], String.valueOf(points), String.valueOf(wins), String.valueOf(podiums)});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Sort drivers list in descending order of points
        Collections.sort(drivers, new Comparator<String[]>() {
            public int compare(String[] s1, String[] s2) {
                return Integer.compare(Integer.parseInt(s2[2]), Integer.parseInt(s1[2]));
            }
        });
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("java_projekt/Tipp1/textContent/Score.csv"))) {
            writer.write("Session 2023");
            writer.newLine();
            for (int i = 0; i < drivers.size(); i++) {
                String[] d = drivers.get(i);
                writer.write((i + 1) + "," + String.join(",", d));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scoreBoard.updateData();
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/textContent/Results.csv"))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] splitLine = line.split(",");
                if (splitLine.length == 2) {
                    String name = splitLine[0];
                    String points = splitLine[1];
                    String formattedLine = "Fahrer: " + name + ", Punkte: " + points;
                    content.append(formattedLine).append("\n");
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        content.append("\n(Deine Ergebnisse werden auch unter Scoreboard angezeigt!)");
        JOptionPane.showMessageDialog(this, content.toString(), "Ergebnisse", 1);
    }
}
