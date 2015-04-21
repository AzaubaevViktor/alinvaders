import java.awt.*;

public class Bomb extends GameObject {
    public long damage = 40;
    private double speed;

    Bomb (Point mouseClick, double speed, Dimension d) {
        pos.setLocation(d.getWidth(), d.getHeight() / 2);
        v.setLocation(mouseClick.X() - d.getWidth(), mouseClick.Y() - d.getHeight() / 2);
        v.normalize(speed);

        life = maxLife = d.getWidth() + d.getHeight() / 2;
        radius = 5;
        this.speed = speed;
    }

    @Override
    public void step(double dt) {
        pos.move(v, dt);
        life -= speed * dt;
    }

    @Override
    public void collisionHandler(GameObject go) {
        life = 0;
    }
}
