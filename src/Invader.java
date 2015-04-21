import java.util.Random;

public class Invader extends GameObject {
    private Point a = new Point();

    Invader(double speed) {
        radius = 10;
        pos.setLocation(Math.random() * 10, Math.random() * 470 + radius / 2);
        v.setLocation(speed, 0);
        life = 100;
    }

    @Override
    public void step(double dt) {
        logic(dt);
    }

    @Override
    public void collisionHandler(GameObject go) {
        Bomb bomb;
        if (go.getClass() == Bomb.class) {
            bomb = (Bomb) go;
            life -= bomb.damage;
        }
    }

    private void logic(double dt) {
        v.rotate((Math.random() * 0.1 - 0.05) * dt);
        pos.move(v, dt);
    }
}
