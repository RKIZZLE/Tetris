import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MyFrame frame = null;
            try {
                frame = new MyFrame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            frame.startGameThread();
        });
    }
}