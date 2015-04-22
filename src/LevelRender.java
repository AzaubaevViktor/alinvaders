import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.Vector;

public class LevelRender extends JPanel implements Runnable, MouseInputListener, MouseMotionListener{
    private int fps = 60;
    private double dt = 1. / fps;

    public long steps = 0;

    private Vector<Invader> invaders;
    private Vector<Bomb> bombs;

    private boolean isNeed2CreateBomb = false;

    private Point mouseClick = new Point();

    private Level level;

    private Dimension screen = new Dimension();

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public LevelRender() {
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
    public void mouseDragged(MouseEvent e) {
        mouseClick.setLocation(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent evt) {
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

    private void drawInvaders(Graphics g) {
        Iterator<Invader> iIt = invaders.iterator();
        Point pos;
        Invader invader;
        int r;

        g.setColor(Color.WHITE);
        while (iIt.hasNext()) {
            invader = iIt.next();

            pos = invader.getPos();
            r = (int) invader.radius;
            g.fillArc(pos.X() -r/2, pos.Y() - r/2, r, r, 0, (int) (invader.life/invader.maxLife*360.));
            g.drawArc(pos.X() - r / 2, pos.Y() - r / 2, r, r, 0, 360);
        }
    }

    private void drawBombs(Graphics g) {
        Iterator<Bomb> bIt = bombs.iterator();
        Point pos;
        Bomb bomb;
        int r;

        g.setColor(Color.GREEN);
        while (bIt.hasNext()) {
            bomb = bIt.next();

            pos = bomb.getPos();
            r = (int) bomb.radius;
            g.fillArc(pos.X() - r / 2, pos.Y() - r / 2, r, r, 0, (int) (bomb.life/bomb.maxLife*360.));
            g.drawArc(pos.X() - r/2, pos.Y() - r/2, r, r, 0, 360);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, (int) screen.getWidth(), (int) screen.getHeight());

        drawInvaders(g);

        drawBombs(g);

        g.setColor(Color.WHITE);
        g.drawString("Killed: " + level.killedInvaders +
                ", Pwned: " + level.pwnedInvader +
                ", Level: " + level.level, 0, 20);
    }

    @Override
    public void run() {
        try {

            if (level == null) {
                while (getSize().getHeight() == 0);
                screen = getSize();
                level = new Level(fps, screen);

                invaders = level.invaders;
                bombs = level.bombs;
            }

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
            System.out.println("wtf: " + e.getMessage() + e.toString());
        }
    }

}
