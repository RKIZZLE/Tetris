import javax.swing.*;
import java.awt.*;


public class Block extends JPanel{

    int blockWidth = 50;
    int blockHeight = 50;


    Block(int x,int y){
        this.setSize(blockWidth,blockHeight);
        this.setBackground(new Color(15, 15, 15));
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        this.setLocation(x,y);
    }

}
