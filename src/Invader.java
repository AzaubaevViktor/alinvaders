import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.Random;

public class Invader extends GameObject {
    private Point a = new Point();
    public AI ai;
    public int vision;

    private GameObject visibleGO;
    private double visibleDist;
    public boolean isRebound = false;


    Invader(double speed, Dimension screen, AI parentAI) {
        radius = 10;
        vision = (int) (radius + speed * 1.5);
        pos.setLocation(Math.random() * 10,
                Math.random() * (screen.getHeight() - radius) + radius / 2);
        v.setLocation(speed, 0);
        life = maxLife = 100;

        if (null != parentAI) {
            ai = new AI(parentAI);
        } else {
            ai = new AI();
        }
    }

    @Override
    public void step(double dt) {
        logic(dt);
    }

    public void good(double val) {
        ai.count += val;
    }

    public void bad(double val) {
        ai.count -= val;
    }

    @Override
    public void collisionHandler(GameObject go) {
        Bomb bomb;
        if (go.getClass() == Bomb.class) {
            bomb = (Bomb) go;
            life -= bomb.damage;
        }
    }

    public void addVisibleObject(GameObject go, double dist) {
        if (dist < visibleDist) {
            visibleGO = go;
        }
    }

    private void logic(double dt) {
        double data[] = new double[7];
        if (null != visibleGO) {
            data[0] = 1;
            Point bmbPosRel = visibleGO.getPos().pBetweenVector(pos);
            data[1] = bmbPosRel.R() / vision;
            data[2] = bmbPosRel.Phi() / Math.PI;
        } else {
            data[0] = 0;
            data[1] = 1;
            data[2] = (Math.random() - 0.5) * 2;
        }
        data[3] = life / maxLife;
        data[4] = v.R() / vision;
        data[5] = v.Phi() / Math.PI;

        double phi = ai.getAPhi(data);

        v.rotate(phi * dt);

        if (Math.abs(v.Phi()) > Math.PI / 4) {
            double newPhi = v.Phi() < 0 ? -1 : 1;
            v.setPhi(newPhi * Math.PI / 4);
        }
        pos.move(v, dt);

        visibleGO = null;
        visibleDist = Double.MAX_VALUE;
        isRebound = false;
    }

    public void horisontalReboundCheck(int minY, int maxY, int minX) {
        isRebound = false;

        if (pos.Y() < minY) {
            pos.setLocation(pos.X(), 2 * minY - pos.Y());
            v.setLocation(v.X(), -v.Y());
            isRebound = true;
        } else if (pos.Y() > maxY) {
            pos.setLocation(pos.X(), 2 * maxY - pos.Y());
            v.setLocation(v.X(), -v.Y());
            isRebound = true;
        }

        if (pos.X() < minX) {
            pos.setLocation(2 * minX - pos.X(), pos.Y());
            v.setLocation(- v.X(), v.Y());
            isRebound = true;
        }

        if (isRebound) {
            life -= maxLife / 2;
        }
    }
}
