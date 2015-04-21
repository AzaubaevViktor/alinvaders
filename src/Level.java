import java.util.Iterator;
import java.util.Vector;

/**
 * Created by `ktulhy` on 4/21/15.
 */
public class Level {
    public long level = 0;
    private int fps;
    private double dt;

    private long invCount;
    private double invCooldown;
    private double invSpeed;

    private double bombCooldown;
    private double bombSpeed;

    public Vector<Invader> invaders = new Vector<Invader>();
    public Vector<Bomb> bombs = new Vector<Bomb>();

    private long stepPrevInvCreate = 0;
    private long stepPrevBombCreate = 0;

    public long killedInvaders = 0;
    public long pwnedInvader = 0;

    public Level(int fps) {
        this.fps = fps;
        dt = 1. / fps;
        levelChange(1);
    }

    public void createInvaders(long steps) {
        invaders.add(new Invader(invSpeed));
        stepPrevInvCreate = steps;
    }

    public void createBomb(long steps, Point mouseClick) {
        bombs.add(new Bomb(mouseClick.X(), mouseClick.Y(), bombSpeed));

        stepPrevBombCreate = steps;
    }

    private void levelChange(long dl) {
        level += dl;

        invCount = (int) (level + Math.sqrt(level));
        invCooldown = (int) (3. / level + 1);
        invSpeed = 10 * Math.pow(2, level - 1);

        bombCooldown = 1. / level;
        bombSpeed = 15 * level;

        System.out.println(bombSpeed + " " + invSpeed);
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

        if (killedInvaders != 0) {
            long newLevel = (long) (Math.log(killedInvaders / 2.) / Math.log(5.)) + 1;
            if (newLevel != level) {
                levelChange(newLevel - level);
            }
        } else {
            level = 1;
        };

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
