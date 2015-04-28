import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame gameFrame = new JFrame();
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setVisible(true);
                gameFrame.setSize(1024, 768);
                gameFrame.getContentPane().add(new LevelRender());

            }
        });
    }
}
