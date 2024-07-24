import javax.swing.*;

public class ScorePanel extends JPanel {

    int score;
    int level;

    ScorePanel(){
        score = 0;
        level = 1;
    }

    public void updateScore(int linesCleared) {
        int points = switch (linesCleared) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };
        score += points;
    }

    public void updateLvl(){
        level++;
    }


    public int getScore() {
        return score;
    }
    public int getLevel() {
        return level;
    }
}