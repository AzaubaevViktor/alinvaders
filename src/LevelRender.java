import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public class LevelRender extends JPanel implements Runnable, MouseInputListener, MouseMotionListener{
    private int fps = 10;
    private double dt = 1. / fps;

    public long steps = 0;

    private Vector<Invader> invaders;
    private Vector<Bomb> bombs;

    private boolean isNeed2CreateBomb = false;

    private Point mouseClick = new Point();

    private Level level = new Level(fps);

    public LevelRender() {
        invaders = level.invaders;
        bombs = level.bombs;

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
        addMouseMotionListener(this);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Drag");
        mouseClick.setLocation(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent evt) {
        System.out.println("Press:" + evt.getPoint().x + ", " + evt.getPoint().y);
        mouseClick.setLocation(evt.getX(), evt.getY());
        isNeed2CreateBomb = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isNeed2CreateBomb = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        int r;
        Point pos;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 640, 480);

        g.setColor(Color.WHITE);
        for (Invader invader: invaders) {
            pos = invader.getPos();
            r = (int) invader.radius;
            g.fillArc(pos.X() -r/2, pos.Y() - r/2, r, r, 0, (int) (invader.life/100.*360.));
            g.drawArc(pos.X() - r/2, pos.Y() - r/2, r, r, 0, 360);
        }

        g.setColor(Color.GREEN);
        for (Bomb bomb: bombs) {
            pos = bomb.getPos();
            r = (int) bomb.radius;
            g.fillArc(pos.X() - r / 2, pos.Y() - r / 2, r, r, 0, (int) (bomb.life/20.*360.));
            g.drawArc(pos.X() - r/2, pos.Y() - r/2, r, r, 0, 360);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                long stTime = System.nanoTime();
                long endTime = 0;
                long dTime = 0;
                level.logic(steps, mouseClick, isNeed2CreateBomb);
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

}
