
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class Data extends JPanel {

    int blockWidth = 200;
    int blockHeight = 1000;
    NextUpPanel nextUpPanel;
    ScorePanel scorePanel;
    JLabel scoreInt;
    JLabel levelInt;



    int dropSpeed = 1000;

    Data(int x, int y, Queue<Character> nextUp) {
        super();
        this.setSize(blockWidth, blockHeight);
        this.setLocation(x, y);
        setLayout(null);

        Border lineBorder = BorderFactory.createLineBorder(Color.white, 1);


        nextUpPanel = new NextUpPanel(nextUp);
        nextUpPanel.setBounds(0, 0, 200, 600);
        nextUpPanel.setBackground(new Color(15, 15, 15));
        nextUpPanel.setBorder(lineBorder);


        JLabel next = new JLabel("Next");
        next.setFont(new Font("Arial", Font.BOLD, 20));
        next.setForeground(Color.WHITE);
        next.setHorizontalAlignment(SwingConstants.CENTER);
        next.setBounds(0, 0, 200, 30);
        next.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        nextUpPanel.setLayout(null);
        nextUpPanel.add(next);


        scorePanel = new ScorePanel();
        scorePanel.setBounds(0, 600, 200, 400);
        scorePanel.setBorder(lineBorder);
        scorePanel.setBackground(new Color(15, 15, 15));
        scorePanel.setLayout(null);

        JLabel scoreStr = new JLabel("Score:");
        scoreStr.setFont(new Font("Arial", Font.BOLD, 20));
        scoreStr.setForeground(Color.WHITE);
        scoreStr.setHorizontalAlignment(SwingConstants.CENTER);
        scoreStr.setBounds(0, 20, 200, 30);
        scorePanel.add(scoreStr);

        scoreInt = new JLabel("0");
        scoreInt.setFont(new Font("Arial", Font.BOLD, 40));
        scoreInt.setForeground(Color.WHITE);
        scoreInt.setHorizontalAlignment(SwingConstants.CENTER);
        scoreInt.setBounds(0, 60, 200, 60);
        scorePanel.add(scoreInt);

        JLabel levelStr = new JLabel("Lvl:");
        levelStr.setFont(new Font("Arial", Font.BOLD, 20));
        levelStr.setForeground(Color.WHITE);
        levelStr.setHorizontalAlignment(SwingConstants.CENTER);
        levelStr.setBounds(0, 195, 200, 30);
        scorePanel.add(levelStr);

        levelInt = new JLabel("1");
        levelInt.setFont(new Font("Arial", Font.BOLD, 40));
        levelInt.setForeground(Color.WHITE);
        levelInt.setHorizontalAlignment(SwingConstants.CENTER);
        levelInt.setBounds(0, 220, 200, 60);
        scorePanel.add(levelInt);


        this.add(nextUpPanel);
        this.add(scorePanel);
    }

    public void updateNextUp(Queue<Character> nextUp) {
        nextUpPanel.setNextUpQueue(new LinkedList<>(nextUp));
    }

    public void updateLevel(){
        scorePanel.updateLvl();
        levelInt.setText(String.valueOf(scorePanel.getLevel()));
        dropSpeed -= (scorePanel.getLevel()*100);
    }

    public void updateScore(int linesCleared) {
        scorePanel.updateScore(linesCleared);
        scoreInt.setText(String.valueOf(scorePanel.getScore()));
    }

    public int getDropSpeed() {
        return dropSpeed;
    }
}