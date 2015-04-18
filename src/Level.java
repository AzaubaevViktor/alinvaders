import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public class Level extends JPanel implements Runnable, MouseInputListener{
    private int mouseX = 0;
    private int mouseY = 0;
    private int fps = 10;
    private double dt = 1. / fps;

    private int level = 1;
    private double invCount = 0;
    private double invCooldown = 0;
    private Vector<Invader> invaders = new Vector<Invader>();
    private Vector<Bomb> bombs = new Vector<Bomb>();

    private long killedInvaders = 0;
    private long steps = 0;
    private long stepPrevInvCreate = 0;
    private boolean isNeed2CreateBomb = false;

    public Level() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        break;
                    case KeyEvent.VK_RIGHT:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
        addMouseListener(this);
        levelChange(0);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent evt) {
        System.out.println("Press:" + evt.getPoint().x + ", " + evt.getPoint().y);
        mouseX = evt.getX();
        mouseY = evt.getY();
        isNeed2CreateBomb = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        Point pos;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 640, 480);

        g.setColor(Color.WHITE);
        for (Invader invader: invaders) {
            pos = invader.getPos();
            g.drawArc(pos.X() - 5, pos.Y() - 5, 10, 10, 0, 360);
        }

        g.setColor(Color.GREEN);
        for (Bomb bomb: bombs) {
            pos = bomb.getPos();
            g.drawArc(pos.X() - 5, pos.Y() - 5, 10, 10, 0, 360);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                long stTime = System.nanoTime();
                long endTime = 0;
                long dTime = 0;
                logic();
                repaint();
                steps++;
                while (true) {
                    endTime = System.nanoTime();

                    dTime = endTime - stTime;
                    if (dTime < dt * 100000000.) {
                        Thread.sleep(dTime / 1000000);
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("wtf" + e.getMessage() + e.toString());
        }
    }

    private void levelChange(int dl) {
        level += dl;
        invCount = (int) level + Math.sqrt(level);
        invCooldown = (int) 3/level + 1;
    }

    private void levelUp() {
        levelChange(1);
    }

    private void logic() {
        if ((invaders.size() < invCount) && (steps - stepPrevInvCreate < invCooldown * fps)) {
            System.out.println("Invader created");
            invaders.add(new Invader());
            stepPrevInvCreate = steps;
        }

        if (isNeed2CreateBomb) {
            System.out.println("Bomb created");
            bombs.add(new Bomb(mouseX, mouseY));

            isNeed2CreateBomb = false;
        }

        for (Invader invader: invaders) {
            invader.step(dt);
        }

        for (Bomb bomb: bombs) {
            bomb.step(dt);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
