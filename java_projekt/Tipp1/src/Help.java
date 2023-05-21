import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Help extends JPanel {
    public Help(MainFrame frame) {
        setLayout(new BorderLayout());

        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(helpText);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));

        add(backButton, BorderLayout.PAGE_END);

        try (BufferedReader reader = new BufferedReader(new FileReader("java_projekt/Tipp1/textContent/Help.txt"))) {
            helpText.read(reader, null);
        } catch (IOException e) {
            helpText.setText("Help file not found!");
        }

    }
}
