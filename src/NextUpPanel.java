import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import javax.imageio.ImageIO;

public class NextUpPanel extends JPanel {

    private Queue<Character> nextUpQueue;
    private BufferedImage oImage, iImage, jImage, lImage, sImage, zImage, tImage;


    public NextUpPanel(Queue<Character> nextUpQueue) {
        this.nextUpQueue = nextUpQueue;

        try {
            oImage = ImageIO.read(new File("src/TetrominoPngs/o.png"));
            iImage = ImageIO.read(new File("src/TetrominoPngs/i.png"));
            jImage = ImageIO.read(new File("src/TetrominoPngs/j.png"));
            lImage = ImageIO.read(new File("src/TetrominoPngs/l.png"));
            sImage = ImageIO.read(new File("src/TetrominoPngs/s.png"));
            zImage = ImageIO.read(new File("src/TetrominoPngs/z.png"));
            tImage = ImageIO.read(new File("src/TetrominoPngs/t.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNextUpQueue(Queue<Character> nextUpQueue) {
        this.nextUpQueue = nextUpQueue;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int y = 65;
        int count = 0;

        for (Character tetromino : nextUpQueue) {
            if (count >= 4) break;

            BufferedImage image = getTetrominoImage(tetromino);
            if (image != null) {
                g.drawImage(image, 50, y, null);
                y += image.getHeight() + 55;
                count++;
            }
        }
    }

    private BufferedImage getTetrominoImage(Character tetromino) {
        switch (tetromino) {
            case 'o':
                return oImage;
            case 'i':
                return iImage;
            case 'j':
                return jImage;
            case 'l':
                return lImage;
            case 's':
                return sImage;
            case 'z':
                return zImage;
            case 't':
                return tImage;
            default:
                return null;
        }
    }
}
