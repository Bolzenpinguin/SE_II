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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("textContent/Tipp.csv"))) {
            for (int i = 0; i < listModel.getSize(); i++) {
                writer.write((i + 1) + "," + listModel.getElementAt(i));
                writer.newLine();
            }
            writer.flush(); // Ensure data is immediately written to the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
