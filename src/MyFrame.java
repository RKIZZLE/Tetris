import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.*;
import java.util.Random;

public class MyFrame extends JFrame implements KeyListener {

    int frameWidth = 716;
    int frameHeight = 1040;

    Queue<Character> nextUp = new LinkedList<>();
    Data dataPanel;

    Tetromino currentTetromino;
    Color color;

    Thread gameThread;
    Thread gravityThread;

    Color backGround = new Color(15, 15, 15);
    Random random = new Random();

    Block[][] gameMatrix = new Block[20][10];
    volatile boolean spawnTimer;
    int baseGravityInterval;

    int sumLinesCleared;

    MyFrame() throws IOException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes x button exit application
        this.setLayout(null); // makes it so no layout is followed
        this.setVisible(true); // makes frame visible
        this.setSize(frameWidth, frameHeight); // window dimensions
        this.setTitle("Tetris"); // title of window
        this.setResizable(false); // doesn't allow resizing the window
        this.getContentPane().setBackground(Color.black); // changes background color

        ImageIcon image = new ImageIcon("src/icon.png"); // create ann ImageIcon
        this.setIconImage(image.getImage()); // sets the icon image to the ImageIcon object

        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addKeyListener(this);

        for (int y = 0; y < 1000; y += 50) {
            for (int x = 0; x < 500; x += 50) {
                Block aBlock = new Block(x, y);
                this.add(aBlock);
                gameMatrix[y / 50][x / 50] = aBlock; // Store the panel in the array
            }
        }

        dataPanel = new Data(500, 0, nextUp);
        baseGravityInterval = dataPanel.getDropSpeed();
        this.add(dataPanel);

    }

    public Color colorRNG() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return new Color(red, green, blue);
    }

    public void fillQueue() {
        char[] tetrominoes = {'o','i', 'j', 'l', 's', 'z', 't'};

        if (nextUp.size() < 10) {
            int randomIndex = random.nextInt(tetrominoes.length);
            nextUp.add(tetrominoes[randomIndex]);
        }
        dataPanel.updateNextUp(nextUp);
    }

    public synchronized void spawn() {
        if (spawnTimer) {
            Character nextTetromino = nextUp.poll();
            if (nextTetromino != null) {
                color = colorRNG();
                switch (nextTetromino) {
                    case 'o':
                        currentTetromino = new Tetromino(new int[][]{{0, 5}, {0, 6}, {1, 5}, {1, 6}}, color);
                        break;
                    case 'i':
                        currentTetromino = new Tetromino(new int[][]{{0, 5}, {1, 5}, {2, 5}, {3, 5}}, color);
                        break;
                    case 'j':
                        currentTetromino = new Tetromino(new int[][]{{0, 5}, {1, 5}, {2, 5}, {2, 4}}, color);
                        break;
                    case 'l':
                        currentTetromino = new Tetromino(new int[][]{{0, 5}, {1, 5}, {2, 5}, {2, 6}}, color);
                        break;
                    case 's':
                        currentTetromino = new Tetromino(new int[][]{{0, 5}, {0, 6}, {1, 6}, {1, 7}}, color);
                        break;
                    case 'z':
                        currentTetromino = new Tetromino(new int[][]{{0, 5}, {0, 6}, {1, 4}, {1, 5}}, color);
                        break;
                    case 't':
                        currentTetromino = new Tetromino(new int[][]{{0, 5}, {1, 4}, {1, 5}, {1, 6}}, color);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + nextTetromino);
                }

                if (!currentTetromino.canBePlaced(gameMatrix, backGround, color)) {
                    endGameThread();
                    return;
                }

                currentTetromino.draw(gameMatrix);
                spawnTimer = false;
            } else {
                throw new IllegalStateException("nextUp queue is empty");
            }
        }
    }

    public synchronized void gravity() {
        if (currentTetromino != null && currentTetromino.canMoveDown(gameMatrix, backGround)) {
            currentTetromino.erase(gameMatrix, backGround);
            currentTetromino.moveDown();
            currentTetromino.draw(gameMatrix);
        } else if(currentTetromino == null){
            spawnTimer = true;
        }

        if (currentTetromino != null && !currentTetromino.canMoveDown(gameMatrix, backGround)){
            currentTetromino = null;
        }
    }

    public void clearRow(int row){
        for (int i=0; i<gameMatrix[0].length; i++){
            gameMatrix[row][i].setBackground(backGround);
        }
    }
    public void shift(int row) {
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < gameMatrix[0].length; j++) {
                Color colorAbove = gameMatrix[i - 1][j].getBackground();
                gameMatrix[i][j].setBackground(colorAbove);
            }
        }
        for (int j = 0; j < gameMatrix[0].length; j++) {
            gameMatrix[0][j].setBackground(backGround);
        } //top row cant be switched (out of bounds)
    }

    public void pointGenerator() {
        int linesCleared = 0;
        for (int i = gameMatrix.length - 1; i >= 0; i--) {
            int colorsInRow = 0;
            for (int j = 0; j < gameMatrix[0].length; j++) {
                if (!gameMatrix[i][j].getBackground().equals(backGround)) {
                    colorsInRow++;
                }
            }
            if (colorsInRow == 10 && currentTetromino==null) {
                clearRow(i);
                shift(i);
                linesCleared++;
                sumLinesCleared++;
                i++;
            }
        }
        if (linesCleared > 0) {
            dataPanel.updateScore(linesCleared);
        }
    }

    public void levelProgression(){
        if (sumLinesCleared==10){
            dataPanel.updateLevel();
            baseGravityInterval = dataPanel.getDropSpeed();
            sumLinesCleared=0;
        } else if(sumLinesCleared>10){
            dataPanel.updateLevel();
            baseGravityInterval = dataPanel.getDropSpeed();
            sumLinesCleared -= 10;
        }
    }

    public void startGameThread(){
        gameThread = new Thread(new GameLoop());
        gameThread.start();

        gravityThread = new Thread(new GravityTask());
        gravityThread.start();
    }
    public void endGameThread() {
        gameThread = null;
        gravityThread = null;

        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private class GameLoop implements Runnable {
        @Override
        public void run() {
            while (gameThread != null) {
                fillQueue();
                spawn();
                pointGenerator();
                levelProgression();
                try {
                    Thread.sleep(10); // Main game loop runs every 10 milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class GravityTask implements Runnable {
        @Override
        public void run() {
            while (gravityThread != null) {
                gravity();
                //System.out.println(sumLinesCleared);
                try {
                    Thread.sleep(baseGravityInterval); // Gravity task initially runs every 1000 milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            if (currentTetromino.canMoveLeft(gameMatrix, backGround)){
                currentTetromino.erase(gameMatrix, backGround);
                currentTetromino.moveLeft();
                currentTetromino.draw(gameMatrix);
            }
        } else if (key == KeyEvent.VK_RIGHT) {
            if (currentTetromino.canMoveRight(gameMatrix, backGround)){
                currentTetromino.erase(gameMatrix, backGround);
                currentTetromino.moveRight();
                currentTetromino.draw(gameMatrix);
            }
        } else if (key == KeyEvent.VK_DOWN) {
            if (currentTetromino.canMoveDown(gameMatrix, backGround)){
                currentTetromino.erase(gameMatrix, backGround);
                currentTetromino.moveDown();
                currentTetromino.draw(gameMatrix);
            }

        }else if (key == KeyEvent.VK_UP) {
            if(currentTetromino.canRotate(gameMatrix, backGround)){
                currentTetromino.erase(gameMatrix, backGround);
                currentTetromino.rotate();
                currentTetromino.draw(gameMatrix);
                System.out.println("rotating");
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
