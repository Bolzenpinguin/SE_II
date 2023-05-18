import javax.swing.*;
import java.awt.*;

public class Play extends JPanel {
    public Play(MainFrame frame) {
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> frame.showPanel("MainMenu"));
        add(backButton);


        // Logic? Vorschläge

    }
}
