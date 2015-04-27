import java.awt.*;

public class BulletFromGun extends Bullet {

    BulletFromGun(Point mouseClick, double speed, double damage, Dimension d) {
        pos.setLocation(d.getWidth() * 0.95, d.getHeight() * 0.45);
        v.setLocation(mouseClick.X() - d.getWidth() * 0.95, mouseClick.Y() - d.getHeight() * 0.45);
        v.normalize(speed);

        life = maxLife = (d.getWidth() + d.getHeight() / 2) / speed;
        radius = 2;
        this.damage = damage;
    }

}
