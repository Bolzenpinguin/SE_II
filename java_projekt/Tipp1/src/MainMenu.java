import javax.swing.*;
import java.awt.*;

/**
 * This class is responsible for programming the main menu window.
 * It extends the JPanel class and creates and manages the main menu of the application.
*/

public class MainMenu extends JPanel {
    /**
     * Constructor of the MainMenu class.
     * Creates the main menu window with the corresponding buttons.
     * @param frame The main window of the application.
    */
    public MainMenu(MainFrame frame) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create buttons
        JButton playButton = new JButton("Spielen");
        JButton helpButton = new JButton("Hilfe");
        JButton scoreButton = new JButton("Scoreboard");
        JButton instructionsButton = new JButton("Anleitung");
        JButton quitButton = new JButton("Verlassen");
        
        // Center buttons horizontally
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners to the buttons
        playButton.addActionListener(e -> frame.showPanel("Play"));
        helpButton.addActionListener(e -> frame.showPanel("Help"));
        scoreButton.addActionListener(e -> frame.showPanel("ScoreBoard"));
        instructionsButton.addActionListener(e -> frame.showPanel("Instructions"));
        quitButton.addActionListener(e -> System.exit(0));

        // Add buttons and gaps to the main menu
        add(Box.createVerticalGlue());
        add(playButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scoreButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(instructionsButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(helpButton);
        add(Box.createRigidArea(new Dimension(0,10)));

        add(quitButton);
        add(Box.createVerticalGlue());
    }
}
