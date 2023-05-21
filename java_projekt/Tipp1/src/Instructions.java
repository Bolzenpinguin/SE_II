import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Instructions extends JPanel {
    public Instructions(MainFrame frame) {
        setLayout(new BorderLayout());

        JTextArea instructionText = new JTextArea();
        instructionText.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(instructionText);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/textContent/Instructions.txt"))) {
            instructionText.read(reader, null);
        } catch (IOException e) {
            instructionText.setText("Instructions file not found!");
        }

    }
}