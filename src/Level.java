import java.util.Iterator;
import java.util.Vector;

/**
 * Created by `ktulhy` on 4/21/15.
 */
public class Level {
    private long level = 0;
    private int fps;
    private double dt;

    private long invCount;
    private double invCooldown;

    private double bombCooldown = 1;

    public Vector<Invader> invaders = new Vector<Invader>();
    public Vector<Bomb> bombs = new Vector<Bomb>();

    private long stepPrevInvCreate = 0;
    private long stepPrevBombCreate = 0;

    private long killedInvaders = 0;
    private long pwnedInvader = 0;

    public Level(int fps) {
        this.fps = fps;
        dt = 1. / fps;
        levelChange(1);
    }

    public void createInvaders(long steps) {
        System.out.println("Invader created");
        invaders.add(new Invader());
        stepPrevInvCreate = steps;
    }

    public void createBomb(long steps, Point mouseClick) {
        System.out.println("Bomb created");
        bombs.add(new Bomb(mouseClick.X(), mouseClick.Y()));

        stepPrevBombCreate = steps;
    }

    private void levelChange(int dl) {
        level += dl;
        invCount = (int) (level + Math.sqrt(level));
        invCooldown = (int) (3. / level + 1);
    }

    public void levelUp() {
        levelChange(1);
    }

    private void invaderHandler(Iterator<Invader> it) {
        Invader invader = it.next();

        invader.step(dt);

        if (invader.life <= 0) {
            it.remove();
            killedInvaders++;
        }

        if (invader.getPos().X() > 640) {
            it.remove();
            pwnedInvader++;
        }
    }

    private void bombHandler(Iterator<Bomb> it) {
        Bomb bomb = it.next();

        bomb.step(dt);
        double r = 0;
        for (Invader invader: invaders) {
            r = invader.radius + bomb.radius;
            r *= r;
            if (bomb.getPos().distance2(invader.getPos()) < r) {
                invader.collisionHandler(bomb);
                bomb.collisionHandler(invader);
            }
        }

        if (bomb.life <= 0) {
            it.remove();
        }
    }

    public void logic(long steps, Point mouseClick, boolean isNeed2CreateBomb) {
        if ((invaders.size() < invCount) && (steps - stepPrevInvCreate > invCooldown * fps)) {
            this.createInvaders(steps);
        }

        if (isNeed2CreateBomb && (steps - stepPrevBombCreate > bombCooldown * fps)) {
            createBomb(steps, mouseClick);
        }

        Iterator<Invader> iIt = invaders.iterator();
        while (iIt.hasNext()) {
            invaderHandler(iIt);
        }

        Iterator<Bomb> bIt = bombs.iterator();
        while (bIt.hasNext()) {
            bombHandler(bIt);
        }
    }
}
