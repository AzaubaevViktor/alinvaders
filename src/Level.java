import java.awt.*;
import java.util.*;

public class Level {
    private final Dimension screen;

    public long level = 0;
    private int fps;
    private double dt;

    private long invCount;
    private double invCooldown;
    private double invSpeed;

    private double bulletCooldown;
    private double bulletSpeed;

    private double bombCooldown;
    private double bombSpeed;

    public final Object objectsLock = new Object();

    public ArrayList<Invader> invaders = new ArrayList<Invader>();
    public ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    public ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    public ArrayList<Explosion> explosions = new ArrayList<Explosion>();

    public HashMap<Double, AI> AIStorage = new HashMap<Double, AI>();
    private double countSum = 0;

    private Point bulletStartPos = new Point();

    private long stepPrevInvCreate = 0;
    private long stepPrevBulletCreate = 0;
    private long stepPrevBombCreate = 0;

    public long killedInvaders = 0;
    public long pwnedInvader = 0;
    private long bulletDamage = 0;
    private long bombDamage = 0;

    private long shrapnelDamage = 0;
    private long invLife = 0;

    public Level(int fps, Dimension d) {
        this.fps = fps;
        this.screen = d;
        bulletStartPos.setLocation(d.width * 0.95, d.height * 0.45);

        dt = 1. / fps;
        levelChange(1);
    }

    private void removeOldAI() {
        while (AIStorage.size() > 50) {
            double minCount = Double.MAX_VALUE;

            for (Double count: AIStorage.keySet()) {
                if (count < minCount) {
                    minCount = count;
                }
            }

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
            inv = new Invader(invSpeed, invLife, screen, null);
        } else {
            inv = new Invader(invSpeed, invLife, screen, getParentAI());
        }

        synchronized (objectsLock) {
            invaders.add(inv);
        }

        stepPrevInvCreate = steps;
    }

    public void createBullet(long steps, Point mouseClick) {
        synchronized (objectsLock) {
            bullets.add(new BulletFromGun(mouseClick, bulletSpeed, bulletDamage, screen));
        }

        stepPrevBulletCreate = steps;
    }

    public void createBomb(long steps, Point mouseClick) {
        synchronized (objectsLock) {
            bombs.add(new Bomb(mouseClick, bombSpeed, bombDamage, screen));
        }

        stepPrevBombCreate = steps;
    }

    public void createExplosion(Point p, double damage) {
        synchronized (objectsLock) {
            explosions.add(new Explosion(p, damage * 0.1));
            for (int i = 0; i < damage * 0.9 / shrapnelDamage; i++) {
                Point v = new Point(0, bulletSpeed);
                v.setPhi(Math.random() * Math.PI * 2 - Math.PI);
                Point pos = new Point(p);
                pos.move(v, 1);

                bullets.add(new Shrapnel(pos, v, shrapnelDamage, 4, 2));
            }
        }

    }

    private void levelChange(long dl) {
        level += dl;

        invCount = (int) (level + Math.sqrt(level));
        invCooldown = (int) (3. / level + 1);
        invSpeed = 10 * Math.pow(1.1, level - 1);
        invLife = 50 + 20 * level;

        bulletCooldown = 1. / level;
        bulletSpeed = 15 * level;
        bulletDamage = 15 + 2 * level;

        bombCooldown = 5. / level;
        bombSpeed = 10 * level;
        bombDamage = 100 + 10 * level;

        shrapnelDamage = bulletDamage;
    }

    private void removeGameObjects() {
        ListIterator<Bomb> bIt = bombs.listIterator();
        while (bIt.hasNext()) {
            if (bIt.next().life <= 0) {
                bIt.remove();
            }
        }

        ListIterator<Bullet> bullIt = bullets.listIterator();
        while (bullIt.hasNext()) {
            if (bullIt.next().life <= 0) {
                bullIt.remove();
            }
        }

        ListIterator<Invader> invIt = invaders.listIterator();
        while (invIt.hasNext()) {
            Invader invader = invIt.next();
            if (invader.life <= 0) {
                invader.bad(100);
                if (!invader.isRebound) {
                    killedInvaders++;
                    AIStorage.put(invader.ai.count, invader.ai);
                }
                invIt.remove();
            }
        }

        ListIterator<Explosion> explIt = explosions.listIterator();
        while (explIt.hasNext()) {
            if (explIt.next().life <= 0) {
                explIt.remove();
            }
        }

    }

    private void invaderHandler(GameObject go) {
        if (go.getClass() != Invader.class) {
            return;
        }
        synchronized (objectsLock) {

            Invader invader = (Invader) go;
            invader.horisontalReboundCheck((int) invader.radius / 2,
                    (int) (screen.getHeight() - invader.radius / 2),
                    (int) invader.radius / 2);


            if (invader.getPos().X() > screen.getWidth()) {
                invader.life = 0;
                invader.good(100);
                AIStorage.put(invader.ai.count, invader.ai);

                pwnedInvader++;
            }
        }
    }

    private void handle(ArrayList<GameObject> objs) {
        GameObject obj1, obj2;
        double distBetween2, dist2;

        for (int i = 0; i < objs.size(); i++) {
            obj1 = objs.get(i);
            if (obj1.life == 0) { break; }
            obj1.step(dt);
            invaderHandler(obj1);

            for (int j = i + 1; j < objs.size(); j++) {
                obj2 = objs.get(j);
                if (obj2.life == 0) { break; }

                distBetween2 = obj1.getPos().distance2(obj2.getPos());

                dist2 = obj1.radius * obj1.radius + obj2.radius * obj2.radius;

                if (distBetween2 <= dist2) {

                    if (obj1.getClass() == Bomb.class) {
                        Bomb bomb = (Bomb) obj1;
                        obj1.collisionHandler(obj2);
                        obj2.collisionHandler(obj1);
                        createExplosion(bomb.getPos(), bomb.damage);
                    }

                    if (obj1.getClass() == Invader.class) {
                        obj1.collisionHandler(obj2);
                        obj2.collisionHandler(obj1);
                    }
                }

                if ((obj1.getClass() == Invader.class) &&
                        (distBetween2 < ((Invader) obj1).vision)) {
                    ((Invader) obj1).addVisibleObject(obj2, distBetween2);
                }
            }
        }


    }

    public void logic(long steps, Point mouseClick, boolean isShotBombs, boolean isShotBullets) {

        if (killedInvaders != 0) {
            long newLevel = (long) (Math.log(killedInvaders/10. + 3)/Math.log(2.));
            if (newLevel != level) {
                levelChange(newLevel - level);
            }
        } else {
            level = 1;
        }

        if ((invaders.size() < invCount) && (steps - stepPrevInvCreate > invCooldown * fps)) {
            createInvaders(steps);
        }

        if (isShotBombs && (steps - stepPrevBulletCreate > bulletCooldown * fps)) {
            createBullet(steps, mouseClick);
        }

        if (isShotBullets && (steps - stepPrevBombCreate > bombCooldown * fps)) {
            createBomb(steps, mouseClick);
        }

        ArrayList<GameObject> objs = new ArrayList<GameObject>();

        synchronized (objectsLock) {
            objs.addAll(bombs);
            objs.addAll(invaders);
            objs.addAll(bullets);
            objs.addAll(explosions);
            handle(objs);
            removeGameObjects();
        }
    }
}
