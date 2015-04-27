import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        System.out.println(Math.atan2(-.1, -1)/ Math.PI);
        System.out.println(Bullet.class.isAssignableFrom(BulletFromGun.class));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame gameFrame = new JFrame();
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setVisible(true);
                gameFrame.setSize(640, 480);
                gameFrame.getContentPane().add(new LevelRender());

            }
        });
    }
}
