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

public class Play extends JPanel {
    private DefaultListModel<String> listModel;
    private JList<String> driverList;
    private ScoreBoard scoreBoard;

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
        scrollPane.setPreferredSize(new Dimension(200, 300)); // Set preferred size

        JPanel listPanel = new JPanel();
        listPanel.add(scrollPane);
        add(listPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Simulate race day");
        saveButton.addActionListener(e -> {
            saveOrder();
            updateScore();
        });

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        JButton evaluationButton = new JButton("Evaluate Tips");
        evaluationButton.addActionListener(e -> evaluateTips());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        buttonPanel.add(evaluationButton);

        add(buttonPanel, BorderLayout.PAGE_END);

    }

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

    private void evaluateTips() {
        String tippsFile = "java_projekt/Tipp1/textContent/Tipp.csv";
        String siegerFile = "java_projekt/Tipp1/textContent/Score.csv";

        try (BufferedReader tippsReader = new BufferedReader(new FileReader(tippsFile));
         BufferedReader ergebnisReader = new BufferedReader(new FileReader(siegerFile))) {

        ergebnisReader.readLine(); // Erste Zeile der Ergebnisdatei überspringen

        String tippLine;
        String ergebnisLine;

        while ((tippLine = tippsReader.readLine()) != null) {
            String[] tippData = tippLine.split(",");

            int platzierung = Integer.parseInt(tippData[0]);
            String tippFahrer = tippData[1];

            ergebnisReader.mark(4096); // Markiere die aktuelle Position im Stream

            while ((ergebnisLine = ergebnisReader.readLine()) != null) {
                String[] ergebnisData = ergebnisLine.split(",");
                String ergebnisFahrer = ergebnisData[2]; // Index 2 für den Fahrernamen

                if (tippFahrer.equals(ergebnisFahrer)) {
                    int ergebnisPlatzierung = Integer.parseInt(ergebnisData[0]);

                    int punkteVergeben = berechnePunkte(platzierung, ergebnisPlatzierung);
                    System.out.println("Tipp: " + tippFahrer + ", Punkte: " + punkteVergeben);

                    break; // Fahrer gefunden, Schleife verlassen
                }
            }

            ergebnisReader.reset(); // Setze den Stream auf die markierte Position zurück
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static int berechnePunkte(int tippPlatzierung, int ergebnisPlatzierung) {
    int platzDiff = Math.abs(tippPlatzierung - ergebnisPlatzierung);
    switch (platzDiff) {
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
            return 0; // für platzDiff größer als 4
    }
}

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
        JOptionPane.showMessageDialog(this, "Scoreboard has been updated. Check to see your results.");
    }
}
