import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements Runnable{
    private Thread thread;
    private boolean running;
    private int pos = 0;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                running = true;
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        pos -= 7;
                        break;
                    case KeyEvent.VK_RIGHT:
                        pos += 7;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                running = false;
            }
        });
        thread = new Thread(this);
        thread.start();

    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0,0, 100, 100);
        g.drawArc(0, 0, 100, 100, pos, pos + 1);
    }

    @Override
    public void run() {
        try {
            while (true) {
                draw();
                Thread.sleep(85);
                System.out.print(pos + "\r");
            }
        } catch (Exception e) {
            System.out.println("wtf" + e.getMessage() + e.toString());
        }
    }


    private void draw() {
        repaint();
    }
}
