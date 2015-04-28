public class Bullet extends GameObject {
    public double damage;

    @Override
    public void step(double dt) {
        pos.move(v, dt);
        life -= dt;
    }

    @Override
    public void collisionHandler(GameObject go) {
        try {
            Bullet b = (Bullet) go;
        } catch (ClassCastException e) {
            if (go.getClass() != Explosion.class) {
                life = 0;
            }
        }
    }
}
