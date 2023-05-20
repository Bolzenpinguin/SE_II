import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;

public class Play extends JPanel {
    private DefaultListModel<String> listModel;
    private JList<String> driverList;

    public Play(MainFrame frame) {
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

        // Create a rounded border
        scrollPane.setBorder(new LineBorder(Color.BLACK) {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(lineColor);
                g.drawRoundRect(x, y, width - 1, height - 1, 20, 20);
            }
        });

        JPanel listPanel = new JPanel();
        listPanel.add(scrollPane);
        add(listPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save Order");
        saveButton.addActionListener(e -> saveOrder());

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
    if (platzDiff == 0) {
        return 100; // Richtig getippt, 100 Punkte
    } else if (platzDiff == 1) {
        return 80; // 1 Platz daneben, 80 Punkte
    } else if (platzDiff == 2) {
        return 60; // 2 Plätze daneben, 60 Punkte
    } else if (platzDiff == 3) {
        return 40; // 3 Plätze daneben, 40 Punkte
    } else if (platzDiff == 4) {
        return 20; // 4 Plätze daneben, 20 Punkte
    }
    return 0; // Sonst, 0 Punkte
}

}
