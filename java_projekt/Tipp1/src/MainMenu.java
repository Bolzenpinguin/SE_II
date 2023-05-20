import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    public MainMenu(MainFrame frame) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton playButton = new JButton("Play");
        JButton helpButton = new JButton("Help");
        JButton scoreButton = new JButton("Scoreboard");
        JButton instructionsButton = new JButton("Instructions");
        JButton quitButton = new JButton("Quit");

        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        playButton.addActionListener(e -> frame.showPanel("Play"));
        helpButton.addActionListener(e -> frame.showPanel("Help"));
        scoreButton.addActionListener(e -> frame.showPanel("ScoreBoard"));
        instructionsButton.addActionListener(e -> frame.showPanel("Instructions"));
        quitButton.addActionListener(e -> System.exit(0));

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
