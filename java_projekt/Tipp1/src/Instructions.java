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

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        add(backButton, BorderLayout.PAGE_END);

        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/kurtschubert/Desktop/SE_II/java_projekt/Tipp1/textContent/Instructions.txt"))) {
            instructionText.read(reader, null);
        } catch (IOException e) {
            instructionText.setText("Instructions file not found!");
        }

    }
}