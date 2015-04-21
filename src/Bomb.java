public class Bomb extends GameObject {
    public long damage = 40;

    Bomb (int vx, int vy) {
        pos.setLocation(640, 240);
        v.setLocation(vx - 640, vy - 240);
        v.normalize(30);
        life = 20;
        radius = 5;
    }

    @Override
    public void step(double dt) {
        pos.move(v, dt);
        life -= dt;
    }

    @Override
    public void collisionHandler(GameObject go) {
        life = 0;
    }
}
