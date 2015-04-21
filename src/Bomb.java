public class Bomb extends GameObject {
    public long damage = 40;
    private double speed;

    Bomb (int vx, int vy, double speed) {
        pos.setLocation(640, 240);
        v.setLocation(vx - 640, vy - 240);
        v.normalize(speed);
        life = 600;
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
