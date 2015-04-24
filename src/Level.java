import java.awt.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by `ktulhy` on 4/21/15.
 */
public class Level {
    private final Dimension screen;

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

    public HashMap<Double, AI> AIStorage = new HashMap<Double, AI>();
    private double countSum = 0;

    private long stepPrevInvCreate = 0;
    private long stepPrevBombCreate = 0;

    public long killedInvaders = 0;
    public long pwnedInvader = 0;

    public Level(int fps, Dimension d) {
        this.fps = fps;
        this.screen = d;

        dt = 1. / fps;
        levelChange(1);
    }

    private void removeOldAI() {

        while (AIStorage.size() > 50) {

            double minCount = Double.MAX_VALUE;

            AI badAI = null;

            for (Double count: AIStorage.keySet()) {

                if (count < minCount) {
                    minCount = count;
                }
            }

            System.out.println(minCount);

            AIStorage.remove(minCount);
        }
    }

    public AI getParentAI() {
        long size = AIStorage.size();
        if (size == 0) {
            return null;
        }

        removeOldAI();

        countSum = 1;
        for (Double count: AIStorage.keySet()) {
            countSum += count;
        }

        double rnd = (Math.random() * countSum);

        Double aiC = null;
        for (Double count: AIStorage.keySet()) {
            rnd -= count;
            if (rnd <= 0) {
                aiC = count;
                break;
            }
        }

        return AIStorage.get(aiC);
    }

    public void createInvaders(long steps) {
        Invader inv;

        if (Math.random() < 0.1) {
            inv = new Invader(invSpeed, screen, null);
        } else {
            inv = new Invader(invSpeed, screen, getParentAI());
        }

        invaders.add(inv);
        stepPrevInvCreate = steps;
    }

    public void createBomb(long steps, Point mouseClick) {
        bombs.add(new Bomb(mouseClick, bombSpeed, screen));

        stepPrevBombCreate = steps;
    }

    private void levelChange(long dl) {
        level += dl;

        invCount = (int) (level + Math.sqrt(level));
        invCooldown = (int) (3. / level + 1);
        invSpeed = 10 * Math.pow(2, level - 1);

        bombCooldown = 1. / level;
        bombSpeed = 15 * level;
    }

    public void levelUp() {
        levelChange(1);
    }

    private void invaderHandler(Iterator<Invader> it) {
        Invader invader = it.next();

        invader.step(dt);
        invader.bad(dt);
        invader.horisontalReboundCheck((int) invader.radius / 2,
                (int) (screen.getHeight() - invader.radius / 2),
                (int) invader.radius / 2);

        if (invader.life <= 0) {
            invader.bad(100);
            if (!invader.isRebound) {
                AIStorage.put(invader.ai.count, invader.ai);
                killedInvaders++;
            }
            it.remove();
        }

        if (invader.getPos().X() > screen.getWidth()) {
            invader.good(100);
            AIStorage.put(invader.ai.count, invader.ai);

            it.remove();
            pwnedInvader++;
        }


    }

    private void bombHandler(Iterator<Bomb> it) {
        Bomb bomb = it.next();

        bomb.step(dt);
        double r2 = 0;
        double dist;

        for (Invader invader: invaders) {
            r2 = invader.radius + bomb.radius;
            r2 *= r2;
            dist = bomb.getPos().distance2(invader.getPos());
            if (dist < r2) {
                invader.collisionHandler(bomb);
                bomb.collisionHandler(invader);
                invader.bad((int) bomb.damage);
            }

            if (dist < invader.vision * invader.vision) {
                invader.addVisibleObject(bomb, Math.sqrt(dist));
            }
        }

        if (bomb.life <= 0) {
            it.remove();
        }
    }

    public void logic(long steps, Point mouseClick, boolean isNeed2CreateBomb) {

        if (killedInvaders != 0) {
            long newLevel = (long) (Math.log(killedInvaders/10. + 3)/Math.log(2.));
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
