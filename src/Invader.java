import java.util.Random;

public class Invader extends GameObject {
    private Point a = new Point();

    Invader() {
        pos.setLocation(Math.random() * 10, Math.random() * 470 + 5);
        v.setLocation(20, 0);
    }

    @Override
    public void step(double dt) {
        logic(dt);
    }

    private void logic(double dt) {
        v.rotate((Math.random() * 0.1 - 0.05) * dt);
        pos.move(v, dt);
    }
}
