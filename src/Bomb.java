import java.awt.*;

public class Bomb extends GameObject {
    public double damage = 0;

    public Bomb(Point mouseClick, double speed, double damage, Dimension d) {
        pos.setLocation(d.getWidth() * 0.95, d.getHeight() * 0.55);
        v.setLocation(mouseClick.X() - d.getWidth() * 0.95, mouseClick.Y() - d.getHeight() * 0.55);
        v.normalize(speed);

        life = maxLife = (d.getWidth() + d.getHeight() / 2) / speed;
        radius = 5;
        this.damage = damage;
    }

    @Override
    public void step(double dt) {
        pos.move(v ,dt);
        life -= dt;
    }

    @Override
    public void collisionHandler(GameObject go) {
        life = 0;
    }
}
