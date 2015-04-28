
public class Explosion extends GameObject {
    private double maxDamage;
    public double damage;
    private double integr = 2 / 3 * Math.sqrt(2 / Math.PI);

    Explosion(Point p, double damage) {
        pos = new Point(p);
        this.maxDamage = damage;
        life = 1;
    }

    @Override
    public void step(double dt) {
        life -= dt;
        radius = Math.sqrt(2 * (1 - life) / Math.PI) * maxDamage * 5;
        damage = (integr * (- Math.pow((1 - life) + dt, 1.5)) + Math.pow((1 - life), 1.5));
    }

    @Override
    public void collisionHandler(GameObject go) {

    }
}
