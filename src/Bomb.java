public class Bomb extends GameObject {

    Bomb (int vx, int vy) {
        pos.setLocation(640, 240);
        v.setLocation(vx - 640, vy - 240);
        v.normalize(30);
        life = 640;
    }

    @Override
    public void step(double dt) {
        pos.move(v, dt);
        life -= dt;
    }
}
