import javax.swing.*;
import java.awt.*;

public class Settings extends JPanel {
    public Settings(MainFrame frame) {
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));
        add(backButton);
        // Add other components here
    }
}
